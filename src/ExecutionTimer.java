import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ExecutionTimer {
    static int seconds;

    public static void main(String[] args) throws IOException {
        System.out.println("Enter path to Python interpreter");

        String path;
        try (BufferedReader pathReader = new BufferedReader(
                new InputStreamReader(System.in))){
            path = pathReader.readLine();
        }

        // creating operating system process
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(path, "-m", "timeit", "-r", "10");

        // creating and scheduling timer with task that outputs the number of seconds
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(seconds);
                seconds++;
            }
        }, 0, 1000);

        try {
            // starting Python process
            Process pythonProcess = processBuilder.start();

            // trying to get output of the process
            try (var outputReader = new BufferedReader(
                    new InputStreamReader(pythonProcess.getInputStream()))){
                String line;
                while ((line = outputReader.readLine()) != null){
                    System.out.println(line);
                }
            }
        }
        finally {
            timer.cancel();
        }
    }
}

/*
  Можно было сделать через Thread вместо Timer + TimerTask

  Thread timer = new Thread (() ->
        {
            do {
                if(!Thread.interrupted()) {
                    System.out.println(seconds);
                    seconds++;
                }
                else {
                    return;
                }
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException e) {
                    return;
                }
            }
            while(true);
        }
        );
*/




