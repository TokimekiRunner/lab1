import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;
public class Main {
    private static String fileName = "test.txt";
    private static String resultsFileName = "testResults.txt";
    public static void main(String[] args) {//主函数入口
//        TestFunc3_brige();
//        TestFunc4_generate();
        TestFunc5_path();
//        TestFunc6_generate();

    }


    public static void ReadFile() {
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            //清空之前的文件testResults文件并重新建
            File fileres = new File(fileName.substring(0, fileName.length()-4) +"Results.txt");
            fileres.delete();
            fileres.createNewFile();
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while((tempString = reader.readLine())!=null){
                String s = tempString.replaceAll("[\\p{Punct}]+", " ");  //标点变成空格
            //    System.out.println(s);
                String[] Words = s.trim().split("\\s+");   //按空格分割

                for(int i = 0;i<Words.length;i++)   //正则表达式匹配字母并变成小写
                {
                    Pattern p = Pattern.compile("a-z||A-Z");
                    Matcher m = p.matcher(Words[i]);
                    Words[i] = m.replaceAll("").trim().toLowerCase();
                }
                //System.out.println("line"+line+":"+tempString);


                for(String str :Words){
                    write(fileName.substring(0, fileName.length()-4) +"Results.txt",str+"\r\n");
                }
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    static void write(String fileName,String str){
        try{
            //new File(fileName);
            FileWriter writer = new FileWriter(fileName,true);
            writer.write(str);;
       //     System.out.println("write successfully");//一个单词打印一次
            writer.close();
        }catch(IOException e){//抛出异常，必须的
            e.printStackTrace();
        }
    }

     static CGraph readWordsToGraph() {
         CGraph graph = new CGraph();
         File file = new File(resultsFileName);
         BufferedReader reader = null;
         try {
             reader = new BufferedReader(new FileReader(file));
             String tempString = null;
             String word1 = null;
             String word2 = null;
             while ((tempString = reader.readLine()) != null) {
                 word2 = tempString;
                 if (word1 != null) {
                     graph.selfaddEdge(word1, word2);
                //     System.out.println(word1+word2);
                 }
                 word1 = tempString;
        //         System.out.println(tempString);
             }
             reader.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         graph.selfprintmatrix();//打印邻接矩阵
         return graph;
     }

     static void TestFunc3_brige(){
         ReadFile();
         CGraph graph = readWordsToGraph();
         Scanner scanner = new Scanner(System.in);
         while(true){
             System.out.println("please input two word or (0,0) to exit:");
             String word1 = scanner.next();
             String word2 = scanner.next();
             if(word1.equals("0") && word2.equals("0")) break;
             String res = graph.queryBridgeWords(word1,word2);
             System.out.print(res);
         }
         scanner.close();
         return;
     }

     static void TestFunc4_generate(){
         ReadFile();
         CGraph graph = readWordsToGraph();
         Scanner scanner = new Scanner(System.in);
         while(true){
             System.out.println("please input your text or -1 to exit:");
             String inputText = "";
             inputText = scanner.nextLine();
             if(inputText.equals("-1")) break;
             String res = graph.generateNewText(inputText);
             System.out.print(res+"\n");

         }
         scanner.close();
     }

     static void TestFunc5_path(){
         ReadFile();
         CGraph graph = readWordsToGraph();
         Scanner scanner = new Scanner(System.in);
         while(true){
             System.out.println("please input two word or (0,0) to exit(NW:not a word):");
             String word1 = scanner.next();
             String word2 = scanner.next();
             if(word1.equals("0") && word2.equals("0")) break;
             String res = graph.calcShortestPath(word1,word2);
             System.out.print(res);
         }
         scanner.close();
         return;
     }

    static void TestFunc6_generate(){
        ReadFile();
        CGraph graph = readWordsToGraph();
        graph.Randomwalk();
    }
}
