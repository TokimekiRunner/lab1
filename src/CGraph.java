import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CGraph {
    private int vertex;//num of vertex
//    private int edge;//num of edge
    private  int MAX = 200;
//    private LinkedList[] adj = new LinkedList[MAX];

    String[] index = new String[MAX];//每个单词对应一个索引word->index转换表
    int[][] Matrix = new int[MAX][MAX];//邻接矩阵，里面存的是权值
    public CGraph(){//初始化
        vertex = 0;
//        edge = 0;
    }


    public void Drawpic(int [][] Matrix){

        String filePath = "./res/graph.dot";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("digraph G {\n");

            int numVertices = Matrix.length;
            for (int i = 0; i < numVertices; i++) {
                for (int j =  0;j < numVertices; j++) {
                    if (Matrix[i][j] != 0) {
                        writer.write("  " + index[i] + " -> " + index[j] + ";\n");
                    }
                }
            }

            writer.write("}");
            System.out.println("DOT file generated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while generating the DOT file.");
            e.printStackTrace();
        }
        String dotFilePath = filePath;
        String outputFilePath = "./res/example.png";

        try {
            String command = "dot.exe -Tpng " + dotFilePath + " -o " + outputFilePath;
//            String command = "dot -Tpng " + dotFilePath + " -o " + outputFilePath;
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("DOT file converted to PNG successfully.");
            } else {
                System.out.println("Failed to convert DOT file to PNG. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while executing the command.");
            e.printStackTrace();
        }
    }
    public void Randomwalk(){
        String filePath = "Randomwalk.txt";
        int startNode = (int) (Math.random() * MAX);
        List<Integer> visitedNodes = new ArrayList<>();  // 记录经过的节点
        List<int[]> visitedEdges = new ArrayList<>();  // 记录经过的边

        int currentNode = startNode;
        boolean stop = false;

        while (!stop) {
            visitedNodes.add(currentNode);

            // 获取当前节点的出边
            int[] edges = Matrix[currentNode];

            // 随机选择下一个节点
            List<Integer> possibleNextNodes = new ArrayList<>();
            for (int i = 0; i < MAX; i++) {
                if (edges[i] != 0) {
                    possibleNextNodes.add(i);
                }
            }

            if (possibleNextNodes.isEmpty()) {
                // 当前节点不存在出边，停止游走
                stop = true;
            } else {
                // 随机选择下一个节点
                int randomIndex = (int) (Math.random() * possibleNextNodes.size());
                int nextNode = possibleNextNodes.get(randomIndex);

                // 检查是否出现重复边
                int[] targetPair = new int[]{currentNode, nextNode};
                //不能使用List.contain判断是否包括,故使用改
                Optional<int[]> first = visitedEdges.stream()
                        .filter(pair -> Objects.equals(pair[0], targetPair[0]) && Objects.equals(pair[1], targetPair[1]))
                        .findFirst();
                if (first.isPresent()) {
                 //   System.out.println("STOP!!!!");
                    stop = true;
                    currentNode = nextNode;
                    visitedNodes.add(currentNode);//补充最后一个点
                } else {                visitedEdges.add(new int[]{currentNode, nextNode});
                    currentNode = nextNode;
                }
            }
        }



            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for(int i = 0;i<visitedNodes.size();i++){
                System.out.print(index[visitedNodes.get(i)]+" ");
                writer.write(index[visitedNodes.get(i)]+" ");}
                System.out.println("Write Successfully!");

            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

        //System.out.println("Visited Nodes: " + visitedNodes);
//        for (int[] pair : visitedEdges) {
//            System.out.print("First element: " + pair[0]);
//            System.out.println("Second element: " + pair[1]);
//        }
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
    //查询桥接词，返回所有存在的桥接词
    public ArrayList<String> queryBridgeWordsAll(String word1,String word2){
        ArrayList<String> tmpres = new ArrayList<>();
        int index1 = -1,index2 = -1,indexB;
        //求出两个单词的编号
        for(int i=0;i<vertex;i++){
            if(word1.equals(index[i])){
                index1 = i;
            }
            if(word2.equals(index[i])){
                index2 = i;
            }
        }
        if(index1==-1 || index2==-1) return tmpres;
        //遍历index1发出的边的另一个顶点，判断是否可能成为bridge
        for(int i=0;i<vertex;i++){
            if(Matrix[index1][i]!=0){
                if(Matrix[i][index2]!=0)
                    tmpres.add(index[i]);
            }
        }
        return tmpres;
    }


    //查询桥接词，返回随机一个桥接词
    public String queryBridgeWordsOne(String word1,String word2){
        String res = "";
        ArrayList<String> tmpres = queryBridgeWordsAll(word1,word2);
        int num = tmpres.size();
        if(num==0) return res;
        Random random = new Random();
        res = tmpres.get(random.nextInt(tmpres.size()));
        return res;
    }
    //查询桥接词，返回最终结果信息文本
    public String queryBridgeWords(String word1, String word2){
        String res = "";
        ArrayList<String> tmpres = new ArrayList<>();
        int index1 = -1,index2 = -1,indexB;
        //求出两个单词的编号
        for(int i=0;i<vertex;i++){
            if(word1.equals(index[i])){
                index1 = i;
            }
            if(word2.equals(index[i])){
                index2 = i;
            }
        }
        if(index1==-1 || index2==-1) {
            if(index1==-1 && index2==-1) return String.format("No \"%s\" and \"%s\" in the graph!\n",word1,word2);
            else if(index1==-1) return String.format("No \"%s\" in the graph!\n",word1);
            else return String.format("No \"%s\" in the graph!\n",word2);
        }
        tmpres = queryBridgeWordsAll(word1,word2);
        if(tmpres.size()==0){
            res = String.format("No bridge words from \"%s\" to \"%s\"!\n",word1,word2);
        }else{
            if(tmpres.size()==1) return String.format("The bridge words from \"%s\" to \"%s\" is:%s\n",word1,word2,tmpres.get(0));
            res += String.format("The bridge words from \"%s\" to \"%s\" are:",word1,word2);
            for(int i=0;i<tmpres.size()-1;i++){
                res += tmpres.get(i) + ", ";
            }
            res += String.format("and %s.\n", tmpres.get(tmpres.size()-1));
        }
        return res;
    }

    //根据输入文本以及已知图，插入bridge word
    public String generateNewText(String inputText){
        String res = "";
        String[] words = inputText.split(" ");  //doubt:will this always work?
//        String[] words = inputText.split("[^(a-zA-Z)]+");  //doubt:will this always work?
        for(int i=0;i<words.length;i++){
            words[i] = words[i].toLowerCase();
        }
        if(words.length<=1) return inputText;
//        String pWord;
        res += words[0];
        for(int i=0;i<words.length-1;i++){
//            pWord = words[i];
            String bridge = queryBridgeWordsOne(words[i],words[i+1]);
            if(bridge.equals("")){
                res = res + " " + words[i+1];
            }else{
                res = res + " " + bridge + " " + words[i+1];
            }
        }
        return res;
    }
    //根据paths，找出所有路径
    public String getPaths(ArrayList<int[]> paths,int index1,int index2){
        String res = "";
        for(int i=0;i<paths.size();i++){
            int pathLength = 0;
            String tmpres = "";
            Stack<Integer> stack = new Stack<>();
            stack.push(index2);
            int p = paths.get(i)[index2];
            while(p!=index1){
                stack.push(p);
                p = paths.get(i)[p];
            }
//            stack.push(index1);
            tmpres = tmpres + index[index1];
            int preindex = index1;
            //将一条路径加入res
            while(!stack.isEmpty()){
                int tmpindex = stack.pop();
                tmpres = tmpres + "->" + index[tmpindex];
                pathLength += Matrix[preindex][tmpindex];
                preindex = tmpindex;
            }
            tmpres += String.format("[length:%s]",pathLength);
            tmpres += "\n";
            if(!res.contains(tmpres))
                res += tmpres;
        }
        return res;
    }
    //得到word1到word2的所有路径
    public String getPathsTwoWords(String word1,String word2){
        String res = "";
        int index1=-1,index2=-1;
        //找到word编号
        for(int i=0;i<vertex;i++){
            if(index[i].equals(word1)) index1 = i;
            if(index[i].equals(word2)) index2 = i;
        }
        if(index1==-1 || index2==-1) return String.format("%s cannot get to %s",word1,word2);
        ArrayList<boolean[]> visited = new ArrayList<>();  //可能有多条路径
        ArrayList<int[]> paths = new ArrayList<>();
        ArrayList<int[]> distant = new ArrayList<>();
        visited.add(new boolean[vertex]);
        paths.add(new int[vertex]);
        distant.add(new int[vertex]);
        //初始化
        for(int i=0;i<vertex;i++){
            visited.get(0)[i] = false;
            paths.get(0)[i] = -1;
            distant.get(0)[i] = Integer.MAX_VALUE;
        }
        visited.get(0)[index1] = true;
        paths.get(0)[index1] = index1; //or -1 ???
        for(int i=0;i<vertex;i++){
            if(i==index1) continue;
            if(Matrix[index1][i]!=0){
                distant.get(0)[i] = Matrix[index1][i];
                paths.get(0)[i] = index1;
            }
//            distant.get(0)[i] = Matrix[index1][i]==0?Integer.MAX_VALUE:Matrix[index1][i];
//            paths.get(0)[i] = -1;
        }
        //不断更新,遍历路径情况
        for(int i=0;i<visited.size();i++){
            //对每条路径，dijkstra 算法
            int origin_size = visited.size();  //原来的总分支数
            for(int v=0;v<vertex;v++){
                //找当前最小
                int mindist = Integer.MAX_VALUE;
                int minnum = 0;
                ArrayList<Integer> minindex = new ArrayList<>();
                for(int j=0;j<vertex;j++){
                    if(visited.get(i)[j]==false && distant.get(i)[j]<mindist){
                        minindex.clear();
                        mindist = distant.get(i)[j];
                        minnum = 1;
                        minindex.add(j);
                    }else if (visited.get(i)[j]==false && distant.get(i)[j]==mindist){
                        minnum++;
                        minindex.add(j);
                    }
                }
                //如果有多个最小,进行分支然后各自更新,否则直接更新
                if(minnum==0){  //全部都已经访问了
                    continue;
                }else if(minnum>1){  //分支出minnum-1个新的分支
                    for(int k=0;k<minnum-1;k++){
                        visited.add(new boolean[vertex]);
                        paths.add(new int[vertex]);
                        distant.add(new int[vertex]);
                        for(int p=0;p<vertex;p++){  //copy status
                            visited.get(visited.size()-1)[p] = visited.get(i)[p];
                            paths.get(paths.size()-1)[p] = paths.get(i)[p];
                            distant.get(distant.size()-1)[p] = distant.get(i)[p];
                        }
                        //更新状态（新的分支）
                        visited.get(visited.size()-1)[minindex.get(k+1)] = true;
                        for(int j=0;j<vertex;j++){
                            if(visited.get(visited.size()-1)[j]==true) continue;
                            if(Matrix[minindex.get(k+1)][j]==0) continue;  //不可达
                            if(distant.get(distant.size()-1)[j]>(distant.get(distant.size()-1)[minindex.get(k+1)]+Matrix[minindex.get(k+1)][j])){
                                distant.get(distant.size()-1)[j] = distant.get(distant.size()-1)[minindex.get(k+1)]+Matrix[minindex.get(k+1)][j];
                                paths.get(paths.size()-1)[j] = minindex.get(k+1);
                            }
                        }
                    }
                }
                //更新状态（原来的分支）
                visited.get(i)[minindex.get(0)] = true;
                for(int j=0;j<vertex;j++){
                    if(visited.get(i)[j]==true) continue;
                    if(Matrix[minindex.get(0)][j]==0) continue; //不可达
                    if(distant.get(i)[j]>(distant.get(i)[minindex.get(0)]+Matrix[minindex.get(0)][j])){
                        distant.get(i)[j] = distant.get(i)[minindex.get(0)]+Matrix[minindex.get(0)][j];
                        paths.get(i)[j] = minindex.get(0);
                    }
                }
            }
            int now_size = visited.size();  //现在的总分支数 now_size - origin_size = 本次产生的分支
            //处理可能的重复分支
            boolean repeat = false;
            for(int j=0;j<i;j++){
                boolean tmpflag = true;
                for(int k=0;k<vertex;k++){
                    if(paths.get(j)[k]!=paths.get(i)[k]){
                        tmpflag = false;
                        break;
                    }
                }
                if(tmpflag){
                    repeat = true;  //说明此时分支i与之前的分支重复，应当删除由分支i产生的所有子分支
                    break;
                }
            }
            if(repeat){
                visited.subList(origin_size,now_size).clear();
                paths.subList(origin_size,now_size).clear();
                distant.subList(origin_size,now_size).clear();
            }
        }
        //根据paths，找出所有路径
        if(distant.get(0)[index2]==Integer.MAX_VALUE) return String.format("%s cannot get to %s\n",word1,word2);
        res = getPaths(paths,index1,index2);
        return res;
    }
    //得到某个单词到其余所有单词的所有最短路径
    public String getPathsOneWord(String word){
        String res = "";
        int index1 = -1;
        for(int i=0;i<vertex;i++){
            if(word.equals(index[i])){
                index1 = i;
            }
        }
        if(index1==-1) return String.format("%s not in graph",word);
        for(int i=0;i<vertex;i++){
            if(i==index1)   continue;
            res = res + String.format("%s get to %s path:\n",word,index[i]);
            res = res + getPathsTwoWords(word,index[i]);
            res += "\n";
        }
        return res;
    }

    public String calcShortestPath(String word1, String word2){
        String res = "";
        int mode = 1; //1:展示word1到word2的所有路径 2:只有一个word
        if(word1.equals("NW") && word2.equals("NW")) return res;  //input err
        if(word1.equals("NW") || word2.equals("NW")){
            mode = 2;
        }
        if(mode == 1){
            //得到word1到word2的所有路径
            res = getPathsTwoWords(word1,word2);
        }else{
            //得到word到其余每个word的路径
            String word = word1.equals("")?word2:word1;
            res = getPathsOneWord(word);
        }
        return res;
    }
}