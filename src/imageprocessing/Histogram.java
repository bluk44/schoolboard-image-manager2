package imageprocessing;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Histogram {
	
	private int[][] histogram;
	private double[] mean;
	private int type;
	private int nBands;
	
	public Histogram(BufferedImage image){
		histogram = new int[image.getRaster().getNumBands()][256];
		type = image.getType();
		nBands = image.getRaster().getNumBands();
		
		Raster r = image.getRaster();
		for(int i = 0; i < r.getHeight(); i++){
			for(int j = 0; j < r.getWidth(); j++){
				for(int b = 0; b < r.getNumBands(); b++){
					++histogram[b][r.getSample(j, i, b)];
				}
			}
		}
		mean = new double[r.getNumBands()];
		
		for(int i = 0; i < mean.length; i++){
			double mn = 0, sum = 0;
			for(int j = 0; j < 256; j++){
				mn+=histogram[i][j]*j;
				sum+=histogram[i][j];
			}
			mn /= sum;
			mean[i] = mn;
		}
	}
	
	public int getType(){
		return type;
	}
	
	public String getTypeString(){
		switch(type){
		case BufferedImage.TYPE_3BYTE_BGR:
			return "TYPE_3BYTE_BGR";
		case BufferedImage.TYPE_BYTE_GRAY:
			return "TYPE_BYTE_GRAY";
		case BufferedImage.TYPE_INT_RGB:
			return "TYPE_INT_RGB";
		case BufferedImage.TYPE_INT_BGR:
			return "TYPE_INT_BGR";
		}
		return "OTHER_TYPE";
	}
	
	public int getNumBands(){
		return nBands;
	}
	
	public double getMean(int band){
		return mean[band];
	}
	
	public double getTotalMean(){
		double total = 0;
		for(int i = 0; i < mean.length; i++){
			total += mean[i];
		}
		total /= mean.length;
		return total;
	}
	
	public int[] getHistogram(int band){
		return histogram[band];
	}
	
	public int[][] getHistogram(){
		return histogram;
	}
	
}
