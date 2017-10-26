import java.util.Random;
import java.math.BigDecimal; 

public class Pos_AHP {
	private final double[][] A = new double[][]
		{ 
		{ 1, 1/6.0, 1/8.0 },  
        { 6, 1, 1/4.0 },  
        { 8, 4, 1 } 
        };  //判断矩阵
    private final int n=A.length;    
	private final int step=3000; //迭代次数
	private final int PosNum=70; //粒子数(种群规模)
	private double w;//惯性权重
	private final double c1=2;//局部权重参数
	private final double c2=2;//全局权重参数
	    
	private double[] g_best; //全局最优解
	private double[] p_best=new double[n];;//粒子本身历史最优解
	private double[][] p_v=new double[PosNum][n];//粒子速度
	private double[][] p_pos=new double[PosNum][n];//粒子位置
	    
	private Random random=new Random();
	    
   public static void main(String args[])
   {
	   Pos_AHP pos=new Pos_AHP();
       pos.Initialize();
       pos.Search();
   }
   //初始化
   public void Initialize()
   {
	   int r=random.nextInt(n);
       for(int i=0;i<PosNum;i++)
       {
           for(int j=0;j<n;j++){
        	   p_pos[i][j]=random.nextDouble();   
           }
           p_pos[i]=nor(p_pos[i]);
           for(int j=0;j<r;j++){
        	   p_v[i][j]=random.nextDouble()/10-0.1;   
           }
           for(int j=r;j<n;j++){
        	   p_v[i][j]=random.nextDouble()/10;
           }
       }
       p_best=p_pos[0].clone();
       for(int i=1;i<PosNum;i++){
    	   if(function(p_best)>function(p_pos[i]))
           	{
    		   p_best=p_pos[i].clone();
            }  
       }
       g_best=p_best.clone();
   }
   
   public void Search()
   { 
       for(int j=0;j<step;j++)  //迭代
       {
    	   w=0.9-(j/step)*0.5;
           for(int i=0;i<PosNum;i++)  //更新
           {
        	   for(int k=0;k<n;k++){
        		   p_v[i][k]=w*p_v[i][k]+c1*random.nextDouble()*(p_best[k]-p_pos[i][k])+c2*random.nextDouble()*(g_best[k]-p_pos[i][k]);
        		   p_pos[i][k]+=p_v[i][k];
        	   }    
           }
           p_best=p_pos[0].clone();
           for(int i=0;i<PosNum;i++){
        	   if(function(p_best)>function(p_pos[i]))
        	   {
        		   p_best=p_pos[i].clone();
        	   }
           }
           /* for(int i=0;i<PosNum;i++)  
           {
        	   System.out.print(p_best[i]+" "); 
           }
           System.out.println("***");
           for(int i=0;i<n;i++){
        	   System.out.print(g_best[i]+" ");
           }
           System.out.println("******");*/
           if(function(g_best)>function(p_best))
           {
//        	   System.out.println("全局："+function(g_best)+" "+function(p_best));
               g_best=p_best.clone();
           }
           
           
//           for(int i=0;i<PosNum;i++)  
//           {
//        	   for(int k=0;k<n;k++){
//        		   System.out.println( p_v[i][k]+" "+p_pos[i][k]);
//        	   }    
//           }
       }

       g_best=nor(g_best);
       for(int i=0;i<n;i++){
    	   g_best[i]=round(g_best[i],4);
    	   System.out.print(g_best[i]+" ");
       }
       System.out.println("*****************");
       System.out.println(function(g_best));
   }
   public double function(double[] x)
   {
       double y=0;
       double tmp=0;
       for(int i=1;i<n;i++){
    	   for(int j=1;j<n;j++){
    		   tmp+=A[i][j]*x[j];
    	   }
    	   y+=Math.abs(tmp-n*x[i]);
       }
       return y/n;
   }
   // 归一化处理  
   public double[] nor(double[] x)
   {
	   double sum=0;
	   for(int i=0;i<x.length;i++){
		   sum+=Math.abs(x[i]);
	   }
	   for(int i=0;i<x.length;i++){
		   x[i]=Math.abs(x[i])/sum;
	   }
       return x;
   }
   /** 
    * 四舍五入 
    *  
    * @param v 
    * @param scale 
    * @return 
    */  
   public double round(double v, int scale) {  
       if (scale < 0) {  
           throw new IllegalArgumentException(  
                   "The scale must be a positive integer or zero");  
       }  
       BigDecimal b = new BigDecimal(Double.toString(v));  
       BigDecimal one = new BigDecimal("1");  
       return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
   }  
}
