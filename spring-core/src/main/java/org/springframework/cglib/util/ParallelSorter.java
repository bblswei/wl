package org.springframework.cglib.util;

import java.util.Comparator;
import org.springframework.cglib.util.SorterTemplate;
import org.springframework.cglib.util.ParallelSorter.ByteComparer;
import org.springframework.cglib.util.ParallelSorter.ComparatorComparer;
import org.springframework.cglib.util.ParallelSorter.Comparer;
import org.springframework.cglib.util.ParallelSorter.DoubleComparer;
import org.springframework.cglib.util.ParallelSorter.FloatComparer;
import org.springframework.cglib.util.ParallelSorter.Generator;
import org.springframework.cglib.util.ParallelSorter.IntComparer;
import org.springframework.cglib.util.ParallelSorter.LongComparer;
import org.springframework.cglib.util.ParallelSorter.ObjectComparer;
import org.springframework.cglib.util.ParallelSorter.ShortComparer;

public abstract class ParallelSorter extends SorterTemplate {
	protected Object[] a;
	private Comparer comparer;

	public abstract ParallelSorter newInstance(Object[] arg0);

	public static ParallelSorter create(Object[] arrays) {
		Generator gen = new Generator();
		gen.setArrays(arrays);
		return gen.create();
	}

	private int len() {
		return ((Object[]) ((Object[]) this.a[0])).length;
	}

	public void quickSort(int index) {
		this.quickSort(index, 0, this.len(), (Comparator) null);
	}

	public void quickSort(int index, int lo, int hi) {
		this.quickSort(index, lo, hi, (Comparator) null);
	}

	public void quickSort(int index, Comparator cmp) {
		this.quickSort(index, 0, this.len(), cmp);
	}

	public void quickSort(int index, int lo, int hi, Comparator cmp) {
		this.chooseComparer(index, cmp);
		super.quickSort(lo, hi - 1);
	}

	public void mergeSort(int index) {
		this.mergeSort(index, 0, this.len(), (Comparator) null);
	}

	public void mergeSort(int index, int lo, int hi) {
		this.mergeSort(index, lo, hi, (Comparator) null);
	}

	public void mergeSort(int index, Comparator cmp) {
		this.mergeSort(index, 0, this.len(), cmp);
	}

	public void mergeSort(int index, int lo, int hi, Comparator cmp) {
		this.chooseComparer(index, cmp);
		super.mergeSort(lo, hi - 1);
	}

	private void chooseComparer(int index, Comparator cmp) {
		Object array = this.a[index];
		Class type = array.getClass().getComponentType();
		if (type.equals(Integer.TYPE)) {
			this.comparer = new IntComparer((int[]) ((int[]) array));
		} else if (type.equals(Long.TYPE)) {
			this.comparer = new LongComparer((long[]) ((long[]) array));
		} else if (type.equals(Double.TYPE)) {
			this.comparer = new DoubleComparer((double[]) ((double[]) array));
		} else if (type.equals(Float.TYPE)) {
			this.comparer = new FloatComparer((float[]) ((float[]) array));
		} else if (type.equals(Short.TYPE)) {
			this.comparer = new ShortComparer((short[]) ((short[]) array));
		} else if (type.equals(Byte.TYPE)) {
			this.comparer = new ByteComparer((byte[]) ((byte[]) array));
		} else if (cmp != null) {
			this.comparer = new ComparatorComparer((Object[]) ((Object[]) array), cmp);
		} else {
			this.comparer = new ObjectComparer((Object[]) ((Object[]) array));
		}

	}

	protected int compare(int i, int j) {
		return this.comparer.compare(i, j);
	}
}