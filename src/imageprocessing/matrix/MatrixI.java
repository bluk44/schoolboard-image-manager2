package imageprocessing.matrix;

import java.awt.image.BufferedImage;
import java.io.PrintStream;

public class MatrixI {
	protected int sizeX, sizeY;
	protected int[] data;
	
	public MatrixI(){};
	
	public MatrixI(int sizeX, int sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		data = new int[sizeX*sizeY];
	}
	
	public MatrixI(int[] data, int sizeX, int sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		this.data = new int[sizeX*sizeY];
		
		for(int i=0;i<data.length;i++){
			this.data[i] = data[i];
		}
	}
	
	public void fill(int val){
		for (int i = 0; i < data.length; i++) {
			data[i] = val;
		}
	}
	
	public int getElement(int x, int y){
		return data[y*sizeX+x];
	}
	
	public int getElement(int pos){
		return data[pos];
	}
	
	public void setElement(int x, int y, int element){
		data[y*sizeX+x] = element;
	}
	
	public void setElement(int pos, int element){
		data[pos] = element;
	}
	
	public int[] getContents(){
		return data;
	}
	
	public void setContents(int[] data){
		for(int i=0;i<data.length;i++){
			this.data[i] = data[i];
		}
	}
	
	public void setData(int[] data){
		this.data = data;
	}
	
	public int getSizeX(){
		return sizeX;
	}
	
	public int getSizeY(){
		return sizeY;
	}
	
	public int getLength(){
		return data.length;
	}
	
	public MatrixI subMatrix(int x, int y, int sizeX, int sizeY){
		return new MatrixI(data, sizeX, sizeY);
		
	}
	
	public MatrixI transpone(){
		MatrixI T = new MatrixI(getSizeY(), getSizeX());
		for(int i = 0; i< getSizeY(); i++){
			for(int j = 0; j < getSizeX(); j++){
				T.setElement(i, j, getElement(j,i));
			}
		}
		
		return T;
	}
	
	public void threshold(int t){
		for (int i = 0; i < data.length; i++) {
			if(data[i] < t) data[i] = 0;
		}
	}
	
	MatrixI conv(MatrixI kernel){
		// TODO
		return null;
	}
	
	public void set(int val, int x, int y, int w, int h){
		int idxW = 0, idxH = 0;
		for(int i = y; i < y+h+1; i++){
			for(int j = x; j < x+w+1; j++){
				idxW = j;
				idxH = i;
				if(idxH > sizeY) idxH -= sizeY;
				if(idxW > sizeX) idxW -= sizeX;
				setElement(idxW, idxH, val);	
			}
		}
	}
	
	public void setNhood(int val, int x, int y, int rw, int rh){
		int idxW = 0, idxH = 0;
		for(int i = y - rh; i <= y + rh; i++){
			for(int j = x - rw; j <= x +rw; j++){
				idxW = j;
				idxH = i;
				if(idxH > sizeY) idxH -= sizeY;
				else if (idxH < 0) idxH += sizeY;
				if(idxW > sizeX) idxW -= sizeX;
				else if (idxW < 0 ) idxW += sizeX;
				setElement(idxW, idxH, val);
			}
		}
	}
	
	public MatrixI convY(MatrixI kernel, boolean replicate){
		MatrixI result = new MatrixI(sizeX, sizeY);
		MatrixI tmp = new MatrixI(1, sizeY + kernel.getSizeY() - 1);
	
		int ckEl = (kernel.getSizeY() + 1) / 2;
		int bkDist = ckEl;
		int ekDist = kernel.getSizeY() - ckEl - 1;
		
		double sum = 0;
		for(int i=0; i<getSizeX(); i++){
			
			for(int j=0;j<getSizeY(); j++){
				tmp.setElement(0, ekDist + j, getElement(i, j));
			}
			
			if(replicate){
				for(int j=0;j<ekDist;j++){
					tmp.setElement(0, j, getElement(i, 0));
				}
				for(int j=tmp.getSizeY() - ckEl ;j<tmp.getSizeY();j++){
					tmp.setElement(0, j, getElement(i, getSizeY() - 1));
				}					
			}
			
			for(int j=0;j<getSizeY(); j++){
				sum = 0;
				for(int k = -ekDist; k <= bkDist; k++){
					sum += tmp.getElement(0, ekDist + j+k)*kernel.getElement(0, ckEl - k);
				}
				result.setElement(i, j, (int)sum);
			}			
			
		}
		return result;
		
	}
	
	public MatrixI convX(MatrixI kernel, boolean replicate){
		MatrixI result = new MatrixI(sizeX, sizeY);
		MatrixI tmp = new MatrixI(sizeX + kernel.getSizeX() - 1, 1);
	
		int ckEl = (kernel.getSizeX() + 1) / 2;
		int bkDist = ckEl;
		int ekDist = kernel.getSizeX() - ckEl - 1;
		
		double sum = 0;
		for(int i=0; i<getSizeY(); i++){

			for(int j=0;j<getSizeX(); j++){
				tmp.setElement(ekDist + j, 0, getElement(j, i));
			}
			
			if(replicate){
				for(int j=0;j<ekDist;j++){
					tmp.setElement(j, 0, getElement(0, i));
				}
				for(int j=tmp.getSizeX() - ckEl ;j<tmp.getSizeX();j++){
					tmp.setElement(j, 0, getElement(getSizeX() - 1, i));
				}					
			}
			
			for(int j=0;j<getSizeX(); j++){
				sum = 0;
				for(int k = -ekDist; k <= bkDist; k++){
					sum += tmp.getElement(ekDist + j+k, 0)*kernel.getElement(ckEl - k, 0);
				}
				result.setElement(j, i, (int)sum);
			}			
			
		}
		return result;
		
	}
	
	public void print(int x, int y, int w, int h){
		int max = 0;
		for(int i = 0; i < data.length; i++){
			if(data[i] > max) max = data[i];
		}
		int maxPlaces = countPlaces(max);
		
		for(int i = y; i < y + h; i++){
			for(int j = x; j < x + w; j++){
				int dp = maxPlaces - countPlaces(getElement(j, i));
				String s = "";
				for(int pl = 0; pl < dp; pl++){
					s+=" ";
				}
				System.out.print(s + getElement(j, i)+" ");
			}
			System.out.println();
		}
	}
	
	public void print(PrintStream os){
		int max = 0;
		for(int i = 0; i < data.length; i++){
			if(data[i] > max) max = data[i];
		}
		int maxPlaces = countPlaces(max);
		
		for(int i=0;i<sizeY;i++){
			for(int j=0;j<sizeX;j++){
				int dp = maxPlaces - countPlaces(getElement(j, i));
				String s = "";
				for(int pl = 0; pl < dp; pl++){
					s+=" ";
				}
				os.print(s + getElement(j, i)+" ");
			}
			os.println();
		}
	}
	
	private int countPlaces(int number){
		if(number < 10){
			return 1;
		} else if(number < 100){
			return 2;
		} else if(number < 1000){
			return 3;
		} else if(number < 10000){
			return 4;
		} else return 5;
	}
	public void printRow(int r){
		for(int i=0;i<sizeX;i++){
			System.out.print(getElement(i, r)+" ");
		}
	}
	
	public void printCol(int c){
		for(int i=0;i<sizeY;i++){
			System.out.println(getElement(c, i)+" ");
		}
	}
	
	public BufferedImage getBufferedImage(){
		BufferedImage b = new BufferedImage(getSizeX(), getSizeY(), BufferedImage.TYPE_BYTE_GRAY);
		for(int i = 0; i < getSizeY(); i++){
			for(int j = 0; j < getSizeX(); j++){
				int pix = getElement(j, i);
				if(pix > 0){
					if(pix > 255) pix = 255;
					b.getRaster().setSample(j, i, 0, pix);
				}
			}
		}
		return b;
	}
	
}
