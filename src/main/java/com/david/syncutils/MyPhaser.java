package com.david.syncutils;

import java.util.concurrent.Phaser;

/**
 * 模拟学生必须完成3场考试（多个phaser修改多个部分）
 * 
 * @author dailiwei
 * 
 */
public class MyPhaser extends Phaser
{
	@Override
	protected boolean onAdvance(int phase, int registeredParties)
	{
		switch (phase)
		{
		case 0:
			return studentArrived();
		case 1:
			return finishFirstExam();
		case 2:
			return finishSecondExam();
		default:
			return true;
		}
	}

	private boolean studentArrived()
	{
		System.out.println("Phaser: The exam is going to start. The students are ready.");
		System.out.println("Phaser: We have " + getRegisteredParties() + " students");
		return false;
	}

	private boolean finishFirstExam()
	{
		System.out.println("Phaser: All the students has finished the first exam.");
		System.out.println("Phaser: It's time for the second one.");
		return false;
	}
	
	private boolean finishSecondExam()
	{
		System.out.println("Phaser: All the students has finished the second exam.");
		System.out.println("Phaser: It's time for the third one.");
		return false;
	}

	private boolean finishExam()
	{
		System.out.println("Phaser: All the students finished the exam.");
		System.out.println("Phaser: Thank you for your time.");
		return true;
	}
}
