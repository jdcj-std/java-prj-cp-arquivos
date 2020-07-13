import java.io.File;

public class catalogaFeV {

    private static class NotDirectoryException extends Exception {
	NotDirectoryException(File arq) {
	    super(arq.getName() + " não é um diretório");
	}
    }

    public static void main(String args[]) {
	String nomedDir;
	File origem;
	Catalogo catalogoOrigem;
	File destino;
	Catalogo catalogoDestino;
	try {
	    if (args != null && args.length == 2) {
		origem = new File(args[0]);
		destino = new File(args[1]);

		if ( origem.isDirectory()) {
		    System.out.println("Origem " + origem.getName());
		    catalogoOrigem = new Catalogo(origem.toPath());
		    catalogoOrigem.toString();
		}
		else {
		    Exception e = new NotDirectoryException(origem);
		    throw e;
		}

		if (destino.isDirectory()) {
		    System.out.println("Destino " + destino.getName());
		    catalogoDestino = new Catalogo(destino.toPath());
		    catalogoDestino.toString();
		}
		else {
		    Exception e = new NotDirectoryException(destino);
		    throw e;
		}

		catalogoDestino.ajustaCatalogo(catalogoOrigem);
	    }
	}
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    System.out.println("Programa de comparação e cópia de arquivos");
	    System.out.println("Modo de uso: Arquivos <dir de origem> <dir de destino>");
	    e.printStackTrace();
	}
    }
}

