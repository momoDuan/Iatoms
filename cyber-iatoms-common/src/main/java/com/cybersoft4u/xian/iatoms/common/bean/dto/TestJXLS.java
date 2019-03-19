package com.cybersoft4u.xian.iatoms.common.bean.dto;

public class TestJXLS {

	private String name;
	private String age;
	private String sex;
	private String educationBackground;
	private int salary;
	private int bonus;
	
	/**
	 * Constructor:
	 */
	public TestJXLS(String name, String age, String sex,
			String educationBackground) {
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.educationBackground = educationBackground;
	}
	
	/**
	 * Constructor:
	 */
	public TestJXLS(String name, int salary, int bonus) {
		this.name = name;
		this.salary = salary;
		this.bonus = bonus;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the educationBackground
	 */
	public String getEducationBackground() {
		return educationBackground;
	}
	/**
	 * @param educationBackground the educationBackground to set
	 */
	public void setEducationBackground(String educationBackground) {
		this.educationBackground = educationBackground;
	}

	/**
	 * @return the salary
	 */
	public int getSalary() {
		return salary;
	}

	/**
	 * @param salary the salary to set
	 */
	public void setSalary(int salary) {
		this.salary = salary;
	}

	/**
	 * @return the bonus
	 */
	public int getBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
}
