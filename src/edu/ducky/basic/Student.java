package edu.ducky.basic;

public class Student {
	private String Name = "Phuong";
	private int Age = 27;

	public void ShowStudent() {
		System.out.println("Name:" + Name);
		System.out.println("Tuoi" + Age);

	}

	public int AddNumber(int a, int b) {

		int sum = a + b;
		return sum;

	}

	public static void main(String argn[]) {

		Student abc = new Student();
		abc.ShowStudent();
		System.out.println("Sum of two numbers: " + abc.AddNumber(3, 4));
	}

}
