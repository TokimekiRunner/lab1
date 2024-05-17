import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class CGraphTest {
    private static String fileName = "test.txt";
    private static String resultsFileName = "testResults.txt";
    @Test
    void randomwalk() {
    }

    @Test
    void queryBridgeWords() {
        ReadFile();
        CGraph graph = readWordsToGraph();
        String res;
        res = graph.queryBridgeWords("seek","to");
        Assertions.assertEquals(res,"No bridge words from \"seek\" to \"to\"!\n");
        res = graph.queryBridgeWords("to","explore");
        Assertions.assertEquals(res,"No bridge words from \"to\" to \"explore\"!\n");
        res = graph.queryBridgeWords("explore","new");
        Assertions.assertEquals(res,"The bridge words from \"explore\" to \"new\" is:strange\n");
        res = graph.queryBridgeWords("new","and");
        Assertions.assertEquals(res,"The bridge words from \"new\" to \"and\" is:life\n");
        res = graph.queryBridgeWords("and","exciting");
        Assertions.assertEquals(res,"No \"exciting\" in the graph!\n");
        res = graph.queryBridgeWords("exciting","synergies");
        Assertions.assertEquals(res,"No \"exciting\" and \"synergies\" in the graph!\n");
    }

    @Test
    void generateNewText() {
        ReadFile();
        CGraph graph = readWordsToGraph();
        String res;
        res = graph.generateNewText("Seek to explore new and exciting synergies");
        Assertions.assertEquals(res,"seek to explore strange new life and exciting synergies");
    }

    @Test
    void calcShortestPath() {
        ReadFile();
        CGraph graph = readWordsToGraph();
        String res;
        res = graph.calcShortestPath("to","and");
        Assertions.assertEquals(res,"to->explore->strange->new->life->and[length:5]\n" +
                "to->seek->out->new->life->and[length:5]\n");
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
}