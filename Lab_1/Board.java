/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author Sony
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    String ab ="";
    String sc ="";
    String[][] cordi;
    int index =0;
    Image star,green,world,node;
    Timer timer;
    int x, y;
    int[] location;
    int[][] map ;
    int[][] check;
    String[] name;
    int g_n =0;
    int count_o =0;
    Point[] ma;
    FileInputStream fstream = null;{
	try {
		fstream = new FileInputStream("out.txt");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  DataInputStream in = new DataInputStream(fstream);
	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
	  try {
		name = gettingname(br);
		check = gettingnode(br,name.length);
		map = gettingmap(br,name.length);
		location = gettingcabs(br,name.length);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
        
        Point[] max = new Point[check.length];{
    	for(int f=0;f<check.length;f++){
    		max[f] = new Point();
    		max[f].x = check[f][0];
    		max[f].y = check[f][1];
    	}
    }
    int[][] point;
    int count =0;
    public Board(String sdd,String dff) {
       // cordi =cc;
        ab = sdd;
        sc = dff;
        int a_b = get_index(name,ab);
	int s_c = get_index(name,sc);
	Vector<Integer> vc = Shortest_path(map,name.length,a_b,s_c);
        ma = getit(vc);
        g_n = ma.length;
        setBackground(Color.BLACK);
        ImageIcon ii =
            new ImageIcon(this.getClass().getResource("car.png"));
        star = ii.getImage();
        ImageIcon i2 =
                new ImageIcon(this.getClass().getResource("green.png"));
         green = i2.getImage();
         ImageIcon i3 =
                 new ImageIcon(this.getClass().getResource("world.jpg"));
          world = i3.getImage();
          ImageIcon i4 =
                  new ImageIcon(this.getClass().getResource("node.png"));
          node= i4.getImage();
        setDoubleBuffered(true);
        point = gettingpoint(max[0],max[1], 40);
        x = y = 10;
       timer = new Timer(100, this);
        int count =0;
       timer.start(); 
    }
    public void paint(Graphics g) {
        super.paint(g);
        int heo = node.getHeight(this);
        int weo = node.getWidth(this);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(world,0,0,this);
         if(index ==g_n-1 && count_o==40){
        	g2d.drawImage(green, x-15, y-15, this);
        }
        else{
        g2d.drawImage(star, x-15, y-15, this);
        }
        g.setColor(Color.GREEN.darker());
        g2d.setColor(Color.BLACK.darker());
        for(int w=0;w<map.length;w++){
        	for(int u=0;u<map.length;u++){
        		if(map[w][u]!=0){
        			map[u][w] =0;
        			g2d.drawLine(max[w].x+(heo/2),max[w].y+(weo/2),max[u].x+(heo/2),max[u].y+(weo/2));
        		}
        	}
        }
        for(int b=0;b<check.length;b++){
            g2d.setColor(Color.black.darker());
            g2d.drawString(name[b],max[b].x-10,max[b].y-5);
            g2d.drawImage(node,max[b].x,max[b].y, this);
            }
        for(int t=0;t<location.length;t++){
        	if(location[t]!=0){
        		g2d.drawImage(green,max[t].x-10,max[t].y-10,this);
        	}
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
       
    }
    public void actionPerformed(ActionEvent e) {
        if(count_o<40 && index!=0){
        	x = point[count_o][0];
        	y = point[count_o][1];
        	count_o++;
        	System.out.println("in1");
        }
        else{
        if(count_o==40 &&index<g_n-1 || index==0){
        	point = gettingpoint(ma[index],ma[index+1],40);
        	index++;
        	count_o=0;
        	System.out.println("in2");
        }
        else{
        	if(index==g_n-2){
        		x = ma[g_n-1].x;
        		y =ma[g_n-1].y;
        		System.out.println("in3");
        	}
        }
        }
        repaint();  
   
        }
    public int[][] gettingpoint(Point p1,Point p2,int n){
    	int[][] point = new int[n+1][2];
    	int temp1 =1;
    	int temp2 =n;
    	int x3 = 0;
    	int y3=0;
    	for(int i=0;i<n;i++){
    		x3 = (temp1*p2.x+temp2*p1.x)/(temp1+temp2);
    		y3 = (temp1*p2.y+temp2*p1.y)/(temp1+temp2);
    		//System.out.println(x3+"  "+y3);
    		point[i][0] = x3;
    		point[i][1] = y3;
    		//System.out.println(point[i][0]+"  "+point[i][1]);
    		temp1++;
    		temp2--;
    	}
    	point[n][0] = p2.x;
    	point[n][1] = p2.y;
    	return point;
    }
    public int[][] gettingmap(BufferedReader br, int node) throws IOException {
     	 String[] lines = new String[node];
     	  int[][] map = new int[node][node]; for(int l=0;l<node;l++){
      		  lines = br.readLine().split(" ");
      		  for(int h=0;h<node;h++){
      			  map[l][h] = Integer.parseInt(lines[h]);
      			  System.out.print(map[l][h]+" ");
      		  }
      	  }
	return map;
    }
    public int[] gettingcabs(BufferedReader br,int node) throws IOException{
    	int[] location = new int[node];
 		  String[] cabsp = new String[node];
 		  cabsp = br.readLine().split(" ");
 		  for(int d=0;d<node;d++){
 			 location[d] = Integer.parseInt(cabsp[d]);
 		  }
		return location;
    }
    public int[][] gettingnode(BufferedReader br,int node) throws IOException{
    	int[][] nodel = new int[node][2];
    	for(int y=0;y<node;y++){
    	  	  String[] words = br.readLine().split(" ");
    	  	  nodel[y][0] = Integer.parseInt(words[0]);
    	  	  nodel[y][1] = Integer.parseInt(words[1]);
    	  	  }
		return nodel;
    	
   }
    public String[] gettingname(BufferedReader br) throws IOException{
    	String nod = br.readLine();
    	int node = Integer.parseInt(Character.toString(nod.charAt(0)));
    	String[] name = new String[node];
    	String com = br.readLine();
    	name = com.split(" ");
    	return name;
    }
    Point[] getit(Vector<Integer> vc){
		Point[] man = new Point[vc.size()];
		for(int f=0;f<vc.size();f++){
    		man[f] = new Point();
    		man[f].x = check[vc.elementAt(f)][0];
    		man[f].y = check[vc.elementAt(f)][1];
    	}
		return man;
	}
    public int get_index(String[] name,String s){
		int ans =0;
		for(int i=0;i<name.length;i++){
			System.out.println(name[i]+"  "+s+"huu");
			if(name[i].equals(s)){
			return i;	
			}
		}
		return 99;	
	}
    static Vector<Integer> Shortest_path(int[][] a,int n,int v1,int v2){
		int[] set = new int[n];
		int[] path = new int[n];
		int[] length = new int[n];
		int[] temp = new int[n];
		int inf =9999999;
		Vector<Integer> vc = new Vector<Integer>();
		int i,j,s,z,tmp,c=0,f=0;
		s=v1;
		z=v2;
		for(i=0;i<n;i++)
		set[i]=0;
		for(i=0;i<n;i++)
		{
		if(a[s][i]==0)/*There is no direct edge between vertices s and i*/
		    {
		length[i]=inf;
		path[i]=0;/*Empty path*/
		      }
		else
		    {
		length[i]=a[s][i];
		path[i]=s;
		     }
		}
		set[s]=1;
		length[s]=0;
		while(set[z]!=1)
		 {
		j=srch_min(length,set,n);
		set[j]=1;
		for(i=0;i<n;i++)
		  {	
		if(set[i]!=1)
		   {
		if(a[i][j]!=0)
	     	  {
		if(length[j]+a[i][j]<length[i])	
		         {
		length[i]=length[j]+a[i][j];
		path[i]=j;
		          }
		        }
		     }
		   }
		}
		j=0;
		i=z;
		while(i!=s)
		{
		tmp=path[i];
		temp[j]=tmp;
		i=tmp;
		j++;
		c++;
		}
		for(j=c-1;j>=0;j--)
		{
			vc.add(temp[j]);
		if(temp[j]==z)
		f=1;
		}
		if(f!=1)
			vc.add(z);
				return vc;
	}
	static int srch_min(int length[],int[] set,int n) 
	{
	int min,i,min_index=90;
	min=99999;
	for(i=1;i<n;i++)
	{
	if(set[i]!=1)
	{
	if(length[i]<min)
	{
	min=length[i];
	min_index=i;
	}
	}
	}
	return min_index;
	}
}