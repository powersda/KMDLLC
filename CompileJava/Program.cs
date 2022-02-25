using System;
using System.Diagnostics;
using System.IO;

namespace CompileJava
{
    class Program
    {
        static void Main(string[] args)
        {
            string pathToDocuments = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments).Replace("\\", "/");
            string directory = pathToDocuments + @"/KDMLLC/src/";

            // Compiling all .java files and redirecting the standard errors to the console
            ProcessStartInfo compile = new ProcessStartInfo();
            compile.FileName = "javac.exe";
            compile.WorkingDirectory = String.Format("{0}", directory);
            compile.Arguments = String.Format("*.java");
            compile.UseShellExecute = false;
            compile.CreateNoWindow = true;
            compile.RedirectStandardOutput = true;
            compile.RedirectStandardError = true;
            using (Process process = Process.Start(compile))
            {
                using (StreamReader reader = process.StandardOutput)
                {
                    if (process.StandardError.ReadLine() != null)
                    {
                        Console.WriteLine(process.StandardError.ReadToEnd());
                    }
                    else
                    {
                        // If no errors, run Main.java in its own CMD window
                        ProcessStartInfo execute = new ProcessStartInfo();
                        execute.FileName = "java.exe";
                        execute.WorkingDirectory = String.Format("{0}", directory);
                        execute.Arguments = "Main";
                        execute.UseShellExecute = true;
                        execute.CreateNoWindow = false;
                        using (Process executeJava = Process.Start(execute))
                        {
                            executeJava.WaitForExit();
                        }
                    }
                }

                // Get an array of all files with .class and delete all of them
                string[] extension = Directory.GetFiles(directory, "*" + ".class");
                for (int i = 0; i < extension.Length; i++)
                {
                    File.Delete(extension[i]);
                }
            }
        }
    }
}
