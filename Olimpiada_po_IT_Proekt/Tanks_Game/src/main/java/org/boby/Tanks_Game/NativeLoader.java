package org.boby.Tanks_Game;

import java.lang.reflect.Field;
import java.util.Arrays;

public class NativeLoader
{
 public static void loadNatives(String nativepath)
 {
     try
     {  

     Field natives=ClassLoader.class.getDeclaredField("sys_paths");

      natives.setAccessible(true);

      String paths[]=(String[])natives.get(null);

      for(int i=0;i<paths.length;i++)
      {
        if(paths[i].equalsIgnoreCase(nativepath))
        {
          return;  
        }    
      }

      String newpaths[]=Arrays.copyOf(paths,paths.length+1);

      newpaths[newpaths.length-1]=nativepath;

      natives.set(null,newpaths); 
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }
 }
}