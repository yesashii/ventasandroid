package mdls.ventas_desa;

public class Titulos {
	
	private String titulo;
	private String subtitulo;
	
	public Titulos()
	{
		
	}
	
	public Titulos(String tit, String sub){
        titulo = tit;
        subtitulo = sub;
    }
 
    public String getTitulo(){
        return titulo;
    }
 
    public String getSubtitulo(){
        return subtitulo;
    }
    
   @Override
   public String toString()
    {
    	return this.subtitulo+" "+this.titulo;
    }
    

}
