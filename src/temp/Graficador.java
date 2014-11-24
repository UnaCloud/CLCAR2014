package temp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Graficador {
	static String[] hypervisores={"vmware","vbox"};
	static int ANCHO=227;
	static int ALTO=198;
	static int SPACING_X=16;
	public static void generarGrafica(Arquitectura[] arquiecturas,BufferedImage imgLeyenda,TituloX titulox,File out)throws Exception{
		BufferedImage imgTituloX=ImageIO.read(new File("graficas","x.png"));
		BufferedImage imgTituloY=ImageIO.read(new File("graficas","y.png"));
		int espacioAdicionalYPorGrafica=0;
		int altoPorGrafica=ALTO+espacioAdicionalYPorGrafica+10;
		BufferedImage bi=new BufferedImage(ANCHO*2+SPACING_X-8, arquiecturas.length*altoPorGrafica+imgLeyenda.getHeight()+imgTituloX.getHeight()+2, BufferedImage.TYPE_INT_RGB);
		Graphics2D g=bi.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,bi.getWidth(),bi.getHeight());
		g.setColor(Color.BLACK);g.setFont(new Font("Time New Roman",Font.BOLD, 11));
		int i=0;
		int y=0;
		char letra='A';
		for(Arquitectura arq:arquiecturas){
			g.drawImage(imgTituloY,0,altoPorGrafica*i+(ALTO-imgTituloY.getHeight())/2,null);
			y+=12;
			for(int e=0;e<hypervisores.length;e++){
				BufferedImage a=ImageIO.read(new File("graficas",arq.filename+" "+hypervisores[e]+".png"));
				g.drawImage(a,SPACING_X+ANCHO*e,y,null);
			}
			y-=12;
			for(int e=0;e<hypervisores.length;e++){
				int x=ANCHO/2+ANCHO*e;
				g.drawString(""+(letra++),9+SPACING_X+x,y+15);
			}
			y+=ALTO+espacioAdicionalYPorGrafica+10;
			System.out.println(altoPorGrafica);
			System.out.println(y);
			i++;
		}
		for(int e=0;e<hypervisores.length;e++){
			g.drawImage(imgTituloX,SPACING_X+15+228*e+(ANCHO-imgTituloX.getWidth())/2,y,null);
		}
		y+=imgTituloX.getHeight();
		g.drawImage(imgTituloX,SPACING_X+(bi.getWidth()-imgTituloX.getWidth())/2,y,null);
		g.drawImage(imgLeyenda,(bi.getWidth()-imgLeyenda.getWidth())/2,y,null);
		g.drawRect((bi.getWidth()-imgLeyenda.getWidth())/2, y, imgLeyenda.getWidth(), imgLeyenda.getHeight()-1);
		ImageIO.write(bi,"PNG",out);
	}
	public static void main(String[] args)throws Exception{
		BufferedImage imgLeyenda1=ImageIO.read(new File("graficas","ley1.png"));
		BufferedImage imgLeyenda2=ImageIO.read(new File("graficas","ley2.png"));
		generarGrafica(new Arquitectura[]{Arquitectura.e7600,Arquitectura.e6300},imgLeyenda2,TituloX.POR_GRAFICA, new File("fig1.png"));
		generarGrafica(new Arquitectura[]{Arquitectura.AMD5000plus},imgLeyenda2,TituloX.POR_GRAFICA, new File("fig2.png"));
		generarGrafica(new Arquitectura[]{Arquitectura.i7_4770,Arquitectura.i7_2600,Arquitectura.i5_660},imgLeyenda1,TituloX.POR_GRAFICA, new File("fig3.png"));
		generarGrafica(new Arquitectura[]{Arquitectura.i7_4770_NTB},imgLeyenda1,TituloX.POR_GRAFICA, new File("fig4.png"));
		generarGrafica(new Arquitectura[]{Arquitectura.i7_4770_NADA},imgLeyenda1,TituloX.POR_GRAFICA, new File("fig5.png"));
	}
	static enum TituloX{
		POR_GRAFICA,POR_FILA,UNA
	}
	
}

