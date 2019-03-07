package model.common;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author farshadnsh
 * The interface Template to be implemented by Spider class.
 */
public interface Template {
     /**
      * Run.
      *
      * @throws IOException          the io exception
      * @throws InterruptedException the interrupted exception
      */
     void run() throws IOException, InterruptedException, ExecutionException;
}
