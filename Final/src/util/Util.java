package util;

import java.util.concurrent.ArrayBlockingQueue;

import Jama.Matrix;

// Author: Eliahu Khalastchi
public class Util {

	public static double stdev(ArrayBlockingQueue<Double> q){
		double x[]=new double[q.size()];
		int i=0;
		for(Double d : q){
			x[i]=d;
			i++;
		}
		return stdev(x);
	}
	
	public static double stdev(double[] x){
		double m=average(x);
		double sum=0;
		for(double d : x)
			sum+=(d-m)*(d-m);
		return Math.sqrt(sum/x.length);
	}

	public static double average(double[] x) {
		double sum=0;
		for(double d : x)
			sum+=d;
		
		return sum/x.length;
	}
	
	public static double pearson(double x[],double y[]){
		double xmean=0,ymean=0;
		for(int i=0;i<x.length;i++){
			xmean+=x[i];
			ymean+=y[i];
		}
		xmean/=x.length;
		ymean/=y.length;
		double xx=0,yy=0,xy=0;
		for(int i=0;i<x.length;i++){
			xx+=(x[i]-xmean)*(x[i]-xmean);
			yy+=(y[i]-ymean)*(y[i]-ymean);
			xy+=(x[i]-xmean)*(y[i]-ymean);
		}
		
		if(Double.isInfinite(xx) || Double.isInfinite(xy) || Double.isInfinite(yy))
			return 0;
		else{
			double r=Math.sqrt(xx*yy);
			
			
			if(r!=0)
				r=xy/r;
			
			if(r>1)
				r=1;
			
			if(r<-1)
				r=-1;

			return r;
			
		}
		
/*		if(Double.isInfinite(xx))
			xx=Double.MAX_VALUE;
		if(Double.isInfinite(xy))
			xy=Double.MAX_VALUE;
		if(Double.isInfinite(yy))
			yy=Double.MAX_VALUE;
		
		double r=Math.sqrt(xx*yy);
		if(r!=0)
			r=xy/r;
		//System.out.println(r+","+xx+","+yy+","+xy);
		return r;*/
	}

	public static double pearson(ArrayBlockingQueue<Double> a, ArrayBlockingQueue<Double> b) {
		double[] a1=new double[a.size()];
		double[] b1=new double[b.size()];
		int i=0;
		for(Double d:a){
			a1[i]=d;
			i++;
		}
		i=0;
		for(Double d:b){
			b1[i]=d;
			i++;
		}
		return pearson(a1, b1);
	}

	public static double getMean(double[] vec){
		boolean b=true;
		for(int i=0;i<vec.length-1;i++)
			if(vec[i]!=vec[i+1])
				b=false;
		if(b)
			return vec[0];

		double mean=0;
		for(int i=0;i<vec.length;mean+=vec[i],i++);
		mean/=vec.length;
		return mean;
	}
	// center around the means
	private static Matrix center(Matrix m,double mean[]){
		Matrix c=new Matrix(m.getRowDimension(),m.getColumnDimension());
		for(int i=0;i<m.getRowDimension();i++)
			for(int j=0;j<m.getColumnDimension();j++)
				mean[j]+=m.get(i, j);

		for(int j=0;j<m.getColumnDimension();j++)
			mean[j]/=m.getRowDimension();
		

		for(int i=0;i<m.getRowDimension();i++)
			for(int j=0;j<m.getColumnDimension();j++)
				c.set(i, j, m.get(i, j)-mean[j]);
		
		return c;
	}
	
	public static double zScore(double vx[],double x){
		
		double mean=getMean(vx);
		if(x==mean)
			return 0;

		
		double sigma=0;
		for(int i=0;i<vx.length;i++)
			sigma+=(vx[i]-mean)*(vx[i]-mean);
		
		sigma/=vx.length;
		
		
		sigma=Math.sqrt(sigma);
		double ret=Math.abs((x-mean)/sigma);
		
		if(Double.isInfinite(ret))
			ret=Double.MAX_VALUE;
		
		if(Double.isNaN(ret))
			ret=0;
		
		return ret;
	}

	
	// mahal distance
	public static double mahalDistance(Matrix a, Matrix b){
		
		if(a.getColumnDimension()==1){
			if (a.getRowDimension()<b.getRowDimension()){
				Matrix tmp=a;
				a=b;
				b=tmp;
			}
			
			double vx[][]=a.transpose().getArray();
			return zScore(vx[0],b.get(0,0));
		}
		
		Matrix means=new Matrix(a.getColumnDimension(),1);
		double[] meanA=new double[a.getColumnDimension()];
		double[] meanB=new double[b.getColumnDimension()];

		Matrix a1=center(a,meanA);
		Matrix b1=center(b,meanB);
		
//		a1.print(a1.getRowDimension(), a1.getColumnDimension());
//		System.out.println();
//		b1.print(b1.getRowDimension(), b1.getColumnDimension());

		Matrix Ca=a1.transpose().times(a1).times(1.0/a.getRowDimension());
		Matrix Cb=b1.transpose().times(b1).times(1.0/b.getRowDimension());
		
//		Ca.print(Ca.getRowDimension(), Ca.getColumnDimension());
//		System.out.println();
//		Cb.print(Cb.getRowDimension(), Cb.getColumnDimension());
		double Car=a.getRowDimension();
		double Cbr=b.getRowDimension();
		Matrix p=null;
		try{
			p=(Ca.times(Car/(Car+Cbr)).plus(Cb.times(Cbr/(Car+Cbr)))).inverse();
		}catch (Exception e){
			return 0.0;			
		}
//		System.out.println();
//		p.print(p.getRowDimension(), p.getColumnDimension());
		
		for(int i=0;i<a.getColumnDimension();i++)
			means.set(i, 0,meanA[i]-meanB[i]);
		
//		System.out.println();
//		means.print(means.getRowDimension(), means.getColumnDimension());
		
		Matrix Mahal=means.transpose().times(p).times(means);
		double ret=Mahal.get(0, 0);
		if(ret<0)
			ret=0;
		double d=Math.sqrt(ret);
		if(Double.isNaN(d)){
			return 0;
		}
		return d;
	}
	
	
	// mahal distance
	public static double mahalDistance(double[]  a, Matrix b){
		
		if(a.length==1){
			double vx[][]=b.transpose().getArray();
			return zScore(vx[0],a[0]);
		}
		
		Matrix means=new Matrix(b.getColumnDimension(),1);
		double[] meanA=a;
		double[] meanB=new double[b.getColumnDimension()];

		double data[][]=new double[1][a.length];
		Matrix a1=new Matrix(data);
		Matrix b1=center(b,meanB);
		
//		a1.print(a1.getRowDimension(), a1.getColumnDimension());
//		System.out.println();
//		b1.print(b1.getRowDimension(), b1.getColumnDimension());

		Matrix Ca=a1.transpose().times(a1);
		Matrix Cb=b1.transpose().times(b1).times(1.0/b.getRowDimension());
		
//		Ca.print(Ca.getRowDimension(), Ca.getColumnDimension());
//		System.out.println();
//		Cb.print(Cb.getRowDimension(), Cb.getColumnDimension());
		double Car=1;
		double Cbr=b.getRowDimension();
		Matrix p=null;
		try{
			p=(Ca.times(Car/(Car+Cbr)).plus(Cb.times(Cbr/(Car+Cbr)))).inverse();
		}catch (Exception e){
			return 0.0;			
		}
//		System.out.println();
//		p.print(p.getRowDimension(), p.getColumnDimension());
		
		for(int i=0;i<a.length;i++)
			means.set(i, 0,meanA[i]-meanB[i]);
		
//		System.out.println();
//		means.print(means.getRowDimension(), means.getColumnDimension());
		
		Matrix Mahal=means.transpose().times(p).times(means);
		double ret=Mahal.get(0, 0);
		if(ret<0)
			ret=0;
		double d=Math.sqrt(ret);
		if(Double.isNaN(d)){
			return 0;
		}
		return d;
	}
		
	public static double[][] transpose(double[][] data) {
		double tdata[][]=new double[data[0].length][data.length];
		for(int i=0;i<data.length;i++)
			for(int j=0;j<data[i].length;j++)
				tdata[j][i]=data[i][j];
		return tdata;
	}
}
