import javax.swing.JOptionPane;

import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.util.*;

//This class shows how to use compute the frequencies of the symbols in a file.
public class Huffman {
	
	FrequencyList fList;
	public Huffman() {
		fList = null;
	}
	
	
	public static void main(String args[]) {
		String fileName = "/home/w0lf7/workspace/a4/src/smalltest.txt";
        Scanner file;
        String s;
        String inputLine;
        int i;
		FrequencyList fList;
		Huffman ft = new Huffman();

		
		ft.fList = new FrequencyList();
		
		fList = ft.fList;
		
        //fileName = JOptionPane.showInputDialog("Enter filename.");
        System.out.println("Input File:"+fileName);
		System.out.println("**********Content of Input File**************");
        try {
            file = new Scanner( new File( fileName ) );  
            
            while (file.hasNextLine()) {		//read in line
            	inputLine = file.nextLine();
            	System.out.println(inputLine);
            	for ( i = 0;i < inputLine.length(); i++) {
            		s = ""+inputLine.charAt(i);
            		fList.insert(s);		//insert symbol into linked list (increment count by 1 if already in list)
            	}
            	s = "\n";				//each line ends with a newline character (which is not captured in inputLine)
				fList.insert(s);		//add it manually
                

				}
			MinHeap tree  = new MinHeap(fList.count);
			Node curr = fList.head;
			while(curr != null)
			{	tree.insert(curr);
				curr = curr.next;
            }
			
			tree.printArray();
			//enCode(tree);
			tree.printArray();
 
			
            System.out.println("**********End of Input File*************");
            System.out.println("Symbols and their frequencies");
            fList.printList();	//print the contents of the linked list of symbols and their frequencies.
            file.close();
 			
        }catch (IOException e) {
              System.out.println("IO Error: " + e.getMessage());    
        }
        catch (Exception e) {
              System.out.println("Error: " + e.getMessage());    
        }
	}

}
//The linked list of nodes containing frequency data
class FrequencyList {
	Node head;
	int count;
	FrequencyList() {
		head = null;
		count = 0;
	}
	
	//search for node in linked list that contains  key. Returns a reference to node if found, else returns null.
	public Node search(String key) {
		Node curr=head;
		while (curr != null) {
			if (key.compareTo(curr.data.symbol) == 0) {
			
				break;
			}
			curr = curr.next;
		}	
		return curr;
	}	
		
	//print contents of linked list.
	public void printList() {
		Node curr;
		
		curr = head;
		
		while (curr != null) {	//print each node
			curr.print();
			curr = curr.next;
		}
	}
	
	//insert a new node if key is not in the linked list.  Otherwise increment frequency by 1.	
	public void insert(String key) {
		Node curr;
		curr = search(key);
		if (curr == null) {		//key not in list, add it to front.
			curr = new Node(key);	//insert new node at front of list
			curr.next = head;
			head = curr;
			count = count + 1;
		}
		else {	//already in list. Increment the frequency by 1.
			curr.data.frequency = curr.data.frequency + 1; //increment frequency of symbol 
		}	
	}

}

//A node of the Linked List class FrequencyList.
class Node {
	FrequencyData data;
	Node next;
	Node left;
	Node right;
	
	Node(String key) {
		data = new FrequencyData(key,1);
	}
	Node(FrequencyData data){
		this.data = data;
	}
	
	void print() {
		data.print();
	}
}

//This class contains a symbol and its frequency
class FrequencyData {
	String symbol;	//using a string type will become handy when build the Huffman encoding tree.
	int frequency;
	
	FrequencyData (FrequencyData data) {
		symbol = data.symbol;
		frequency = data.frequency;
		
	}
	
	FrequencyData(String s, int freq) {
		symbol = s;
		frequency = freq;
	}
	
	//print content of symbol (which may contain more than one character
	//for debugging purposes only.
	void printSymbol() {
		int i;
		int a;
		String s="";
		for (i = 0; i < symbol.length(); i++) {
			a = (int) symbol.charAt(i);
			if (a==10)
				s = s+"<NEWLINE>"; //instead of printing a newline, print <NEWLINE> instead
			else if (a == 32)
				s =s+"<SPACE>";   	//same for space
			else if (a == 9)
				s =s+"<TAB>";		//same for tab. Should probably do same for "escaped" characters
			else
				s = s+ symbol.charAt(i);			
		}
		
		System.out.print(s);		
	}
	
	/*used to print symbol(s) and frequency*/
	/*used for debugging*/
	void print() {

		printSymbol();
		System.out.println(":"+frequency);
	}
	
}
class MinHeap{
    // array storing items in the min-heap
	Node[] arr;
    public int size; // heap size
	FrequencyData[] list;
	
    public MinHeap(int MAXSIZE){
	// construct an array and set the size to 0
	arr = new Node[MAXSIZE];
	list = new FrequencyData[MAXSIZE];
	size = 0;
    }

    public Node min(){
	// return the minimum value in the heap
	return arr[0];
    }

    public Node removeMin(){
	// return and remove the minimum value
	Node result = arr[0];
	arr[0] = arr[size - 1];
	size--;
	downHeap(0);
	
	return result;
    }

    public void upHeap(int index){
	int parentIndex = (index - 1) / 2;
	Node temp;
	while (parentIndex >= 0 && arr[parentIndex].data.frequency > arr[index].data.frequency){
	    temp = arr[index];
	    arr[index] = arr[parentIndex];
	    arr[parentIndex] = temp;
	    index = parentIndex;
	    parentIndex = (index - 1) / 2;
	}
    }

    public void downHeap(int index){
	boolean done = false;
	Node temp;
	int leftIndex, rightIndex, targetIndex;
	
	while (!done){
	    leftIndex = 2*index + 1 ;
	    rightIndex = 2*index + 2;
	    if (leftIndex >= size){
		done = true;
	    } else {
		targetIndex = leftIndex;
		if (rightIndex < size && arr[rightIndex].data.frequency<arr[leftIndex].data.frequency){
		    targetIndex = rightIndex;
		}
		if (arr[index].data.frequency<=arr[targetIndex].data.frequency){
		    done = true;
		} else {
		    temp = arr[index];
		    arr[index] = arr[targetIndex];
		    arr[targetIndex] = temp;
		    index = targetIndex;
		}
	    }
	}
    }

    public void insert(Node value){
	// insert a new value in the heap
	arr[size] = value;
	upHeap(size);
	size++;
    }
    
    public void insert(FrequencyData a){
    	list[size] = a;
    	upHeap(size);
    	size++;    	
    	
    }

    public void printArray(){
	// print the array representation of the heap
	for (int i=0; i<size; i++)
	    System.out.println(arr[i].data.symbol+":"+arr[i].data.frequency);
	System.out.println();
    }
    
	public static void enCode(MinHeap a)
	{	Node deletedOne,deletedTwo,root;
		for(int i = 0;i<a.size;i++){
		deletedOne = a.removeMin();
		deletedTwo = a.removeMin();
		String one = deletedOne.data.symbol;
		String two = deletedTwo.data.symbol;
		int fror = (deletedOne.data.frequency+deletedTwo.data.frequency);
		FrequencyData tem = new FrequencyData(one+two,fror);
		root =  new Node(tem);
		root.left= deletedOne;
		root.right= deletedTwo;
		a.insert(root);
		}
	}
		
	public String assign(String b)
	{
		Node curr = arr[0];
		String result = "";
		Node currL  = curr.left;
		Node currR = curr.right;
		
		while(currL != null||currR != null)
		{
			if(currL != null && currL.data.symbol.indexOf(b)>=0)
			{
				result += "0";
				currR = currL.right;
				currL = currL.left;
				if(currL == null && currR == null)
					return result;
			}
			else{
				if(currR != null && currR.data.symbol.indexOf(b)>=0)
				{
					result += "1";
					currL = currR.left;
					currR = currR.right;
					if(currL == null && currR ==null)
						return result;
				}
			}
		}
		return result;
	}
	public String deCode(String input)
	{
		String decoded="";
		Node curr = arr[0];
		for(int i=0;i<input.length();i++){
			if((""+input.charAt(i)).equals("0")){
				if(curr.left != null)
					curr = curr.left;
				else{
					decoded += curr.data.symbol;
					curr = arr[0];
					i--;
				}
			}
		else if((""+input.charAt(i)).equals("0"))
		{
			if(curr.right !=null)
				curr = curr.left;
			else{
				decoded += curr.data.symbol;
				curr = arr[0];
				i--;
			}
		}
		else{
			System.out.println("Wrong");
		}
		}
		return decoded;
	}
}