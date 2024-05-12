import java.io.File;
import java.util.Random;

public class CGraph {
    private int vertex;//num of vertex
    private int edge;//num of edge
    private  int MAX = 5;
    private LinkedList[] adj = new LinkedList[MAX];

    String[] index = new String[MAX];//每个单词对应一个索引word->index转换表
    int[][] Matrix = new int[MAX][MAX];//邻接矩阵，里面存的是权值
    public CGraph(){//初始化
        vertex = 0;
        edge = 0;
    }
    public void selfprintmatrix(){
        for (int i = 0; i < MAX; i++) {
            for (int j = 0; j < MAX; j++) {
                System.out.print(Matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void selfaddEdge(String word1,String word2){//我写的
        int index1 = -1;
        int index2 = -1;
        for(int i = 0;i<vertex;i++){
            if(index[i].equals(word1)){
                index1 = i;
            }
        }
        if(-1 == index1){
            index[vertex] = word1;
            index1 = vertex;
            vertex++;
        }
        for(int i = 0;i<vertex;i++){
            if(index[i].equals(word2)){
                index2 = i;
            }
        }
        if(-1 == index2){
            index[vertex] = word2;
            index2 = vertex;
            vertex++;
        }
        Matrix[index1][index2]++;
    }
    public void addEdge(String word1,String word2){
        int index1 = -1;
        int index2 = -1;
        //找到单词1对应的节点，或者创建一个
        for(int i = 0;i<vertex;i++){
            if(adj[i].getHead().word.equals(word1)){
                index1 = i;
            }
        }
        if(-1 == index1){
            adj[vertex] = new LinkedList(word1);
            index1 = vertex;
            vertex++;
        }
        //找到单词2对应的节点，或者创建一个
        for(int i = 0;i<vertex;i++){
            if(adj[i].getHead().word.equals(word2)){
                index2 = i;
            }
        }
        if(-1 == index2){
            adj[vertex] = new LinkedList(word2);
            index2 = vertex;
            vertex++;
        }
        for(Node node =adj[index1].getHead().next;node!=null;node=node.next){//权值增加
            if(node.word.equals(word2)){
                node.weight++;
                return;
            }
        }
        adj[index1].addNode(word2,index2);
    }
}

class Node{
    public String word;
    public int weight;
    public Node next;
    public int num;
    public boolean painted;
    public Node(){
        word = null;
        next = null;
        num = weight = 0;
    }
    public Node(String w,int n){
        word = w;
        weight = 1;
        next = null;
        num = n;
    }
}

class LinkedList{
    private Node head = null;
    private Node tail = null;
    public int nodeNum;
    public LinkedList(){
        nodeNum = -1;
    }
    public LinkedList(String w){
        nodeNum = 0;
        Node newNode = new Node(w,-1);
        head = newNode;
        tail = newNode;
    }
    public boolean isEmpty(){
        return head==null;
    }
    public void addNode(String w,int n){//wordnext, indexnext
        Node newNode = new Node(w,n);
        if(isEmpty()){
            head = tail = newNode;
        }else{
            tail.next = newNode;
            tail = newNode;
        }
        nodeNum++;
    }
    public Node getHead(){
        return head;
    }
    public Node getTail(){
        return tail;
    }
}