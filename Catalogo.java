import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;


public class Catalogo {
    Path diretorio;
    Set<Path> catalogo;
    List<String> ext = new ArrayList<String>(Arrays.asList(".jpg", ".jpeg", ".mpeg",
							   ".mp4", ".mkv", ".java", ".png"));

    public Catalogo(Path diretorio) throws IOException {
        this.diretorio = diretorio;
	Catalogo.this.catalogo = new LinkedHashSet<Path>();

	if (Files.isDirectory(diretorio)) {
	    Files.walkFileTree(this.diretorio, new SimpleFileVisitor<Path> () {
		    @Override
		    public FileVisitResult visitFile(Path file,
						     BasicFileAttributes attr) {
			String nome = file.getFileName().toString().toLowerCase();
			for(String fileExt : ext) {
			    // Verfica se o nome do arq termina com extensao
			    if (nome.endsWith(fileExt)) {
				System.out.println("Catalogo - arquivo: " + file.toString());
				Catalogo.this.catalogo.add(file);
			    } 
			}
			return FileVisitResult.CONTINUE;
		    }
		});
	}
    }

    public String toString() {
	StringBuffer retorno = new StringBuffer();

	if (this.catalogo != null && this.catalogo.size() > 0) {
	    for(Path arquivo : this.catalogo) {
		retorno.append(arquivo.toString());
		retorno.append("\n");
	    }
	}

	return retorno.toString();
    }

    public void ajustaCatalogo(Catalogo catalogoOrigem) {
	
	// Percorre a colecao de arquivos do catalogo de origem
	for (Path nomeArquivo : catalogoOrigem.catalogo) {
	    try {
		this.insereNoCatalogo(nomeArquivo);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    // Este metodo eh chamado no catalogo de destino para adicionar um novo arquivo a este catalogo
    private void insereNoCatalogo( Path nomeArquivo) throws IOException { // rever lançament de excecao, deve criar lista de falhas
	if (!this.catalogo.contains(nomeArquivo)) {
	    Path novoArquivo = this.copiaArquivo(nomeArquivo, diretorio);
	    this.catalogo.add(novoArquivo);
	}
    }

    private Path copiaArquivo(Path nomeArquivo, Path diretorio) throws IOException {
	File origem = nomeArquivo.toFile();
	File destino = null;

	if (origem.exists()) {
	    destino = new File(diretorio.getFileName() + "/" + origem.getName());
	    FileChannel chanOrigem = null;
	    FileChannel chanDestino = null;
	    try {
		chanOrigem = new FileInputStream(origem).getChannel();
		chanDestino = new FileOutputStream(destino).getChannel();
		chanOrigem.transferTo(0, chanOrigem.size(), chanDestino);
	    }
	    finally {
		if (chanOrigem != null && chanOrigem.isOpen())
		    chanOrigem.close();
		if (chanDestino != null && chanDestino.isOpen())
		    chanDestino.close();
	    }
	    return destino.toPath();
	} else {
	    throw new FileNotFoundException();
	}
    }
}
