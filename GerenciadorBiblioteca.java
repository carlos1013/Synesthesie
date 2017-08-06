package com.example.cadu.synesthesie;

import java.io.*;
import android.content.*;


public class GerenciadorBiblioteca {

    public GerenciadorBiblioteca(Context context){ //construtor que cria o arquivo caso ele nao exista
        File caminho = context.getFilesDir();
        File arquivo = new File(caminho,"biblioteca.txt");
        String linha;
        try{
            BufferedReader dados = new BufferedReader(new FileReader(arquivo));
            linha = dados.readLine();
            dados.close();
        }
        catch (IOException err){
            criarBiblioteca(context);
        }

    }

    public int [][] inicializar(Context context){ ///mapeia os rgbs do arquivo para um array
        File caminho = context.getFilesDir();
        File arquivo = new File(caminho,"biblioteca.txt");
        String linha;
        int qntd;
        int [] [] rgbs = {{-1}};
        try{
            BufferedReader dados = new BufferedReader(new FileReader(arquivo));
            linha = dados.readLine();
            qntd = Integer.parseInt(linha);
            rgbs = new int [qntd][3] ;
            qntd=0;
            while((linha=dados.readLine())!=null){
                String [] partes = linha.split(" ");
                for (int x=0;x<3;x++){
                    rgbs[qntd][x] = Integer.parseInt(partes[x]);
                }
                qntd++;
            }

            dados.close();
        }
        catch (IOException err){

        }
        return rgbs;
    }

    public String [] inicializarNomes(Context context){ ///mapeia os rgbs do arquivo para um array
        File caminho = context.getFilesDir();
        File arquivo = new File(caminho,"biblioteca.txt");
        String linha,junta;
        int qntd;
        String [] nomes = {"oi"};
        try{
            BufferedReader dados = new BufferedReader(new FileReader(arquivo));
            linha = dados.readLine();
            qntd = Integer.parseInt(linha);
            nomes = new String [qntd];
            qntd=0;
            while((linha=dados.readLine())!=null){
                String [] partes = linha.split(" ");
                junta = partes[3];
                for (int x=4;x<partes.length;x++) {
                    junta = junta+" "+partes[x];
                }
                nomes[qntd] = junta;
                qntd++;
            }

            dados.close();
        }
        catch (IOException err){

        }
        return nomes;
    }

    public void editar(int [] rgb,String texto,Context context){ //troca o nome da cor no arquivo
        copiar(context);
        File caminho = context.getFilesDir();
        File arqDest = new File(caminho,"biblioteca.txt");
        File arqOrig = new File(caminho,"auxiliar.txt");
        String linha;
        int[] aux = {-1,-1,-1};
        try{
            FileWriter dadosDest = new FileWriter(arqDest);
            BufferedReader dadosOrig = new BufferedReader(new FileReader(arqOrig));
            linha=dadosOrig.readLine();
            dadosDest.write(linha+"\n");
            while ((linha=dadosOrig.readLine())!=null){
                String [] partes = linha.split(" ");
                aux[0]=Integer.parseInt(partes[0]);
                aux[1]=Integer.parseInt(partes[1]);
                aux[2]=Integer.parseInt(partes[2]);
                if ((aux[0]==rgb[0]) && (aux[1]==rgb[1]) && (aux[2]==rgb[2])){
                    dadosDest.write(aux[0]+" "+aux[1]+" "+aux[2]+" "+texto+"\n");
                }
                else{
                    dadosDest.write(linha+"\n");

                }
            }
            dadosDest.close();
            dadosOrig.close();
        }
        catch(IOException err){

        }
    }

    public void imprimirArq (Context context){ //imprime o arquivo,usada para fins de teste
        File caminho = context.getFilesDir();
        File arquivo = new File(caminho,"biblioteca.txt");
        String linha;
        try{
            BufferedReader dados = new BufferedReader(new FileReader(arquivo));
            while((linha=dados.readLine())!=null){
                System.out.println(linha);
            }
            dados.close();
        }
        catch (IOException err){

        }

    }

    public void remover (int [] rgb,Context context){ //remove uma cor do arquivo
        copiar(context);
        File caminho = context.getFilesDir();
        File arqDest = new File(caminho,"biblioteca.txt");
        File arqOrig = new File(caminho,"auxiliar.txt");
        String linha;
        int[] aux = {-1,-1,-1};
        try{
            FileWriter dadosDest = new FileWriter(arqDest);
            BufferedReader dadosOrig = new BufferedReader(new FileReader(arqOrig));
            linha=dadosOrig.readLine();
            int qntd = Integer.parseInt(linha);
            qntd--;
            dadosDest.write(Integer.toString(qntd)+"\n");
            while ((linha=dadosOrig.readLine())!=null){
                String [] partes = linha.split(" ");
                aux[0]=Integer.parseInt(partes[0]);
                aux[1]=Integer.parseInt(partes[1]);
                aux[2]=Integer.parseInt(partes[2]);
                if ((aux[0]!=rgb[0]) || (aux[1]!=rgb[1]) || (aux[2]!=rgb[2])){
                    dadosDest.write(linha+"\n");
                }
            }
            dadosDest.close();
            dadosOrig.close();
        }
        catch(IOException err){

        }
    }

    private void copiar(Context context){ ///funcao generica pra uso da classe
        File caminho = context.getFilesDir();
        File arqDest = new File(caminho,"auxiliar.txt");
        File arqOrig = new File(caminho,"biblioteca.txt");
        String linha;
        try{
            FileWriter dadosDest = new FileWriter(arqDest);
            BufferedReader dadosOrig = new BufferedReader(new FileReader(arqOrig));
            while((linha=dadosOrig.readLine())!=null){
                dadosDest.write(linha+"\n");
            }
            dadosOrig.close();
            dadosDest.close();

        }
        catch(IOException err){

        }
    }

    public void adicionar (int [] rgb,String nome,Context context){ ///adiciona cor+nome
        copiar(context);
        File caminho = context.getFilesDir();
        File arqDest = new File(caminho,"biblioteca.txt");
        File arqOrig = new File(caminho,"auxiliar.txt");
        String linha;
        try{
            FileWriter dadosDest = new FileWriter(arqDest);
            BufferedReader dadosOrig = new BufferedReader(new FileReader(arqOrig));
            linha = dadosOrig.readLine();
            int qntd = Integer.parseInt(linha);
            qntd++;
            dadosDest.write(Integer.toString(qntd)+"\n");
            while((linha=dadosOrig.readLine())!=null){
                dadosDest.write(linha+"\n");
            }
            String x = rgb[0]+" "+rgb[1]+" "+rgb[2]+" "+nome+"\n";
            dadosDest.write(x);
            dadosDest.close();
            dadosOrig.close();

        }
        catch(IOException err){

        }
    }

    private void criarBiblioteca(Context context){ ///inicializa a biblioteca com alguns valores
        File path = context.getFilesDir();
        File file = new File(path,"biblioteca.txt");
        try {
            PrintWriter stream = new PrintWriter(file);
            stream.println("3");
            stream.println("31 31 224 Azul");
            stream.println("255 255 255 Branco");
            stream.println("0 0 0 Preto");
            stream.close();

        }
        catch (IOException err){
        }
    }
}