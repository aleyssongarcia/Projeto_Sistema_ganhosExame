import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Main extends JFrame implements ActionListener {
    private JLabel labelValorPorExame;
    private JTextField fieldValorPorExame;
    private JLabel labelNumeroDeExames;
    private JTextField fieldNumeroDeExames;
    private JButton buttonCalcular;
    private JButton buttonGerarPDF;
    private JLabel labelTotalGanhos;

    public Main() {
        setLayout(new GridLayout(5, 2));

        labelValorPorExame = new JLabel("Digite o valor por exame: ");
        add(labelValorPorExame);

        fieldValorPorExame = new JTextField(10);
        add(fieldValorPorExame);

        labelNumeroDeExames = new JLabel("Digite o número de exames realizados: ");
        add(labelNumeroDeExames);

        fieldNumeroDeExames = new JTextField(10);
        add(fieldNumeroDeExames);

        labelTotalGanhos = new JLabel("O valor total a receber pelos exames realizados é: ");
        add(labelTotalGanhos);

        add(new JLabel(""));

        buttonCalcular = new JButton("Calcular");
        buttonCalcular.addActionListener(this);
        add(buttonCalcular);

        buttonGerarPDF = new JButton("Gerar PDF");
        buttonGerarPDF.addActionListener(this);
        add(buttonGerarPDF);

        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCalcular) {
            double valorPorExame = Double.parseDouble(fieldValorPorExame.getText());
            int numeroDeExames = Integer.parseInt(fieldNumeroDeExames.getText());
            double totalGanhos = valorPorExame * numeroDeExames;
            labelTotalGanhos.setText("O valor total a receber pelos exames realizados é: " + totalGanhos);
        } else if (e.getSource() == buttonGerarPDF) {
            String mes = JOptionPane.showInputDialog(this, "Digite o mês em que você está:");

            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jfc.setDialogTitle("Escolha onde salvar o relatório de ganhos:");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos PDF", "pdf");
            jfc.addChoosableFileFilter(filter);

            int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = jfc.getSelectedFile().getPath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                try (PDDocument document = new PDDocument()) {
                    PDPage page = new PDPage();
                    document.addPage(page);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.COURIER, 16);
                        contentStream.newLineAtOffset(100, 700);
                        contentStream.showText("Mês: " + mes);
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Valor unitário por Exames: R$ " + fieldValorPorExame.getText());
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Total de exames: " + fieldNumeroDeExames.getText());
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Valor total a receber pelo total de exames: R$ " + labelTotalGanhos.getText().split(": ")[1]);
                        contentStream.endText();
                    }
                    document.save(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
