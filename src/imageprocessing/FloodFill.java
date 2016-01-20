package imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class FloodFill {
	
	public static void run(BufferedImage image, int startX, int startY, Color target, Color rep){
		Stack s = new Stack(image.getWidth()*image.getHeight());
				
		if(image.getRGB(startX, startY) != target.getRGB()) return;
		s.push(startX, startY);
		
		while(s.ptr > 0){
			int[] c = s.pop();
			image.setRGB(c[0], c[1], rep.getRGB());
			
			if(checkXY(image, c[0]-1, c[1]) && image.getRGB(c[0]-1, c[1]) == target.getRGB())
				s.push(c[0]-1, c[1]);

			if(checkXY(image, c[0]+1, c[1]) && image.getRGB(c[0]+1, c[1]) == target.getRGB())
				s.push(c[0]+1, c[1]);
			
			if(checkXY(image, c[0], c[1]-1) && image.getRGB(c[0], c[1]-1) == target.getRGB())
				s.push(c[0], c[1]-1);
			
			if(checkXY(image, c[0], c[1]+1) && image.getRGB(c[0], c[1]+1) == target.getRGB())
				s.push(c[0], c[1]+1);			
				
			
		}
	}
	
	private static boolean checkXY(BufferedImage image, int x, int y){
		if(x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) return true;
		return false;
	}
	
	private static class Stack{
		public int[][] tab;
		public int ptr = 0;
		
		public Stack(int cap){
			tab = new int[cap][2];
		}
		
		public int[] pop(){
			if(ptr > 0){
				--ptr;
				int[] t = new int[2];
				t[0] = tab[ptr][0];
				t[1] = tab[ptr][1];
				
				return t;
			}
			else return null;
		}
		
		public void push(int x, int y){
			tab[ptr][0] = x;
			tab[ptr][1] = y;
			++ptr;
		}
	}
}
