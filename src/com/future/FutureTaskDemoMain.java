package com.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class FutureTaskDemoMain {

	/** 2 tasks to fetch video uploader's info and video info at the same time*/
	public static void main(String[] args) {
		FutureTaskDemoTask t1 = new FutureTaskDemoTask("https://www.tiktok.com/@alexchen2021");
		FutureTaskDemoTask t2 = new FutureTaskDemoTask("https://vm.tiktok.com/ZSJhMBq86/");

		FutureTask<String> futureTask1 = new FutureTask<>(t1);
		FutureTask<String> futureTask2 = new FutureTask<>(t2);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(futureTask1);
		executor.execute(futureTask2);

		while(true)
		{
			try {
				if(!futureTask1.isDone())
				{
					//to get the video owner's info
					String s = futureTask1.get();
					//System.out.println(s);
					Pattern pattern = Pattern.compile("og:description:(?:['\"])\\w+(?:['\"])");
					Matcher matcher = pattern.matcher(s);
					if(matcher.find())
						System.out.println("(FutureTask1 output) video owner's info: "+matcher.group(0));

				}
				if(!futureTask2.isDone())
				{
					//to get the video title
					String s = futureTask2.get();
					//System.out.println(s);
					Pattern pattern = Pattern.compile("og:title:(?:['\"])\\w+(?:['\"])");
					Matcher matcher = pattern.matcher(s);
					if(matcher.find())
						System.out.println("(FutureTask2 output) video title: "+matcher.group(0));
				}
				if(futureTask1.isDone() && futureTask2.isDone())
				{
					System.out.println("Completed both the FutureTasks, shutting down the executors");
					executor.shutdown();

					//TODO:: combine 2 parts of info and save to db ...

					return;
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}

