import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String fileName = "test.txt";
    private static String resultsFileName = "testResults.txt";
    public static void main(String[] args) {//主函数入口
        ReadFile();
        readWordsToGraph();
    }

    public static void ReadFile() {
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while((tempString = reader.readLine())!=null){
                String s = tempString.replaceAll("[\\p{Punct}]+", " ");  //标点变成空格
                System.out.println(s);
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
            System.out.println("write successfully");//一个单词打印一次
            writer.close();
        }catch(IOException e){//抛出异常，必须的
            e.printStackTrace();
        }
    }

     static void readWordsToGraph() {
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
                     System.out.println(word1+word2);
                 }
                 word1 = tempString;
                 System.out.println(tempString);
             }
             reader.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         graph.selfprintmatrix();//打印邻接矩阵
     }
}
