import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskForMultiCompute {
	public static void main(String args[]){
		FutureTaskForMultiCompute inst = new FutureTaskForMultiCompute();
		List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();

		ExecutorService exec = Executors.newFixedThreadPool(5);
		for(int i=0;i<10;i++){
			FutureTask<Integer> ft = new FutureTask<Integer>(inst.new ComputeTask(i, ""+i));
			taskList.add(ft);
			exec.submit(ft);  //or exec.invokeAll(taskList) after this loopf finish
		}
	
		System.out.println("all the task have been submitted, main thread is doing other things.");	
		Integer totalResult = 0;
		for(FutureTask<Integer> ft : taskList ){
			try{
				totalResult += ft.get();
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch(ExecutionException e){
				e.printStackTrace();
			}
		}

		exec.shutdown();
		System.out.println("multi-task result: "+totalResult);
	}
	
	private class ComputeTask implements Callable<Integer> {
		private Integer result = 0;
		private String taskName = "";
	
		public ComputeTask(Integer iniResult, String taskName){
			this.result = iniResult;
			this.taskName = taskName;
			System.out.println("generate sub-thread task:"+taskName);
		}

		public String getTaskName(){
			return this.taskName;
		}

		@Override
		public Integer call() throws Exception{
			for(int i=0;i<100;i++){
				result =+ i;
			}
			Thread.sleep(5000);
			System.out.println("sub-thread: "+taskName + " finished.");
			return result;
		}
	}
}
