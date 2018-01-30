package org.springframework.cglib.transform;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.springframework.asm.Attribute;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassNameReader;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.transform.AbstractProcessTask;
import org.springframework.cglib.transform.ClassReaderGenerator;
import org.springframework.cglib.transform.ClassTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;

public abstract class AbstractTransformTask extends AbstractProcessTask {
	private static final int ZIP_MAGIC = 1347093252;
	private static final int CLASS_MAGIC = -889275714;
	private boolean verbose;

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	protected abstract ClassTransformer getClassTransformer(String[] arg0);

	protected Attribute[] attributes() {
		return null;
	}

	protected void processFile(File file) throws Exception {
		if (this.isClassFile(file)) {
			this.processClassFile(file);
		} else if (this.isJarFile(file)) {
			this.processJarFile(file);
		} else {
			this.log("ignoring " + file.toURI(), 1);
		}

	}

	private void processClassFile(File file)
			throws Exception, FileNotFoundException, IOException, MalformedURLException {
		ClassReader reader = getClassReader(file);
		String[] name = ClassNameReader.getClassInfo(reader);
		DebuggingClassWriter w = new DebuggingClassWriter(2);
		ClassTransformer t = this.getClassTransformer(name);
		if (t != null) {
			if (this.verbose) {
				this.log("processing " + file.toURI());
			}

			(new TransformingClassGenerator(
					new ClassReaderGenerator(getClassReader(file), this.attributes(), this.getFlags()), t))
							.generateClass(w);
			FileOutputStream fos = new FileOutputStream(file);

			try {
				fos.write(w.toByteArray());
			} finally {
				fos.close();
			}
		}

	}

	protected int getFlags() {
		return 0;
	}

	private static ClassReader getClassReader(File file) throws Exception {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

		ClassReader arg2;
		try {
			ClassReader r = new ClassReader(in);
			arg2 = r;
		} finally {
			in.close();
		}

		return arg2;
	}

	protected boolean isClassFile(File file) throws IOException {
		return this.checkMagic(file, -889275714L);
	}

	protected void processJarFile(File file) throws Exception {
		if (this.verbose) {
			this.log("processing " + file.toURI());
		}

		File tempFile = File.createTempFile(file.getName(), (String) null,
				new File(file.getAbsoluteFile().getParent()));

		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(file));

			try {
				FileOutputStream newFile = new FileOutputStream(tempFile);

				try {
					ZipOutputStream out = new ZipOutputStream(newFile);

					ZipEntry entry;
					while ((entry = zip.getNextEntry()) != null) {
						byte[] bytes = this.getBytes(zip);
						if (!entry.isDirectory()) {
							DataInputStream outEntry = new DataInputStream(new ByteArrayInputStream(bytes));
							if (outEntry.readInt() == -889275714) {
								bytes = this.process(bytes);
							} else if (this.verbose) {
								this.log("ignoring " + entry.toString());
							}
						}

						ZipEntry outEntry1 = new ZipEntry(entry.getName());
						outEntry1.setMethod(entry.getMethod());
						outEntry1.setComment(entry.getComment());
						outEntry1.setSize((long) bytes.length);
						if (outEntry1.getMethod() == 0) {
							CRC32 crc = new CRC32();
							crc.update(bytes);
							outEntry1.setCrc(crc.getValue());
							outEntry1.setCompressedSize((long) bytes.length);
						}

						out.putNextEntry(outEntry1);
						out.write(bytes);
						out.closeEntry();
						zip.closeEntry();
					}

					out.close();
				} finally {
					newFile.close();
				}
			} finally {
				zip.close();
			}

			if (!file.delete()) {
				throw new IOException("can not delete " + file);
			}

			File newFile1 = new File(tempFile.getAbsolutePath());
			if (!newFile1.renameTo(file)) {
				throw new IOException("can not rename " + tempFile + " to " + file);
			}
		} finally {
			tempFile.delete();
		}

	}

	private byte[] process(byte[] bytes) throws Exception {
		ClassReader reader = new ClassReader(new ByteArrayInputStream(bytes));
		String[] name = ClassNameReader.getClassInfo(reader);
		DebuggingClassWriter w = new DebuggingClassWriter(2);
		ClassTransformer t = this.getClassTransformer(name);
		if (t != null) {
			if (this.verbose) {
				this.log("processing " + name[0]);
			}

			(new TransformingClassGenerator(new ClassReaderGenerator(new ClassReader(new ByteArrayInputStream(bytes)),
					this.attributes(), this.getFlags()), t)).generateClass(w);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(w.toByteArray());
			return out.toByteArray();
		} else {
			return bytes;
		}
	}

	private byte[] getBytes(ZipInputStream zip) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(zip);

		int b;
		while ((b = in.read()) != -1) {
			bout.write(b);
		}

		return bout.toByteArray();
	}

	private boolean checkMagic(File file, long magic) throws IOException {
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		boolean arg5;
		try {
			int m = in.readInt();
			arg5 = magic == (long) m;
		} finally {
			in.close();
		}

		return arg5;
	}

	protected boolean isJarFile(File file) throws IOException {
		return this.checkMagic(file, 1347093252L);
	}
}