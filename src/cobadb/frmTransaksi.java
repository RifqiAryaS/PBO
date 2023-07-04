/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cobadb;

import java.awt.print.PrinterException;
   import java.sql.*;
import java.text.MessageFormat;
import java.util.Calendar;
   import javax.swing.*;
   import javax.swing.table.DefaultTableModel;

/**
 *
 * @author overm
 */
public class frmTransaksi extends javax.swing.JFrame {

    Connection Con;
    ResultSet RsBrg;
    ResultSet RsKons;
    Statement stm;
    double total=0;
    double pajak=0;
    String tanggal;
    Boolean edit=false;
    DefaultTableModel tableModel = new DefaultTableModel(
	new Object [][] {},
	new String [] {
	"Kd Barang", "Nama Barang","Harga Barang","Jumlah","Total","Pajak"
	});
    
    //Var Pencarian Kode Barang
    String idBrg;
    String namaBrg;
    String hargaBrg;
    
    public frmTransaksi() {
        initComponents();
        open_db();
        inisialisasi_tabel();
        aktif(false);
        setTombol(true);
        txtTgl.setEditor(new JSpinner.DateEditor(txtTgl,"yyyy/MM/dd"));
    }

    private void open_db()
    {
        try{
            KoneksiMysql kon = new KoneksiMysql("localhost","root","","pbo");
            Con = kon.getConnection();
        }
        catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }
    
   
    //method hitung penjualan
private void hitung_jual()
{
    double xtot,xhrg,xpjk;
    int xjml;

    xhrg=Double.parseDouble(txtHarga.getText());
    xjml=Integer.parseInt(txtJml.getText());
    xtot=(xhrg*xjml)*90/100;
    String xtotal=Double.toString(xtot);
    xpjk=((xhrg*xjml)*10/100);
    String xpajak=Double.toString(xpjk);
    txtTot.setText(xtotal);
    txtPajak.setText(xpajak);
    total=total+xtot;
    txtTotal.setText(Double.toString(total));
}

//methohd baca data konsumen
private void baca_konsumen()
{
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

    try {
        stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        RsKons = stm.executeQuery("SELECT kd_kons FROM konsumen");

        while (RsKons.next()) {
            String kodeKonsumen = RsKons.getString("kd_kons");
            model.addElement(kodeKonsumen);
        }

				RsKons.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    cmbKd_Kons.setModel(model);
}

private void baca_barang() {
DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

try {
    stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    RsBrg = stm.executeQuery("SELECT kd_brg FROM barang");

    while (RsBrg.next()) {
        String kodeBarang = RsBrg.getString("kd_brg");
        model.addElement(kodeBarang);
    }

    RsBrg.close();
} catch (SQLException e) {
    e.printStackTrace();
}

cmbKd_Brg.setModel(model);
}

//method baca barang setelah combo barang di klik
private void detail_barang(String xkode){            
    try {
        stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        RsBrg = stm.executeQuery("select * from barang where kd_brg = '" +xkode+ "'");

        if (RsBrg.next()) {
            String namaBrg = RsBrg.getString("nm_brg");
            int hargaBrg = RsBrg.getInt("harga");

            txtNm_Brg.setText(namaBrg);
            txtHarga.setText(Integer.toString(hargaBrg));
        } else {
            txtNm_Brg.setText("");
            txtHarga.setText("");
        }

        RsBrg.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e);
    }
}

//method baca konsumen setelah combo konsumen di klik
private void detail_konsumen(String xkode)
{
    try {
        stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        RsKons = stm.executeQuery("select * from konsumen where kd_kons = '" +xkode+ "'");

        if (RsKons.next()) {
            String namaKons = RsKons.getString("nm_kons");
            txtNm_Brg.setText(namaKons);
        } else {
            txtNm_Brg.setText("");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e);
    }
}

//method set model tabel
public void inisialisasi_tabel()
{
    tblJual.setModel(tableModel);
}

//method pengkosongan isian
private void kosong()
{
    txtNoJual.setText("");
    txtNm_Brg.setText("");
    txtHarga.setText("");
    txtTotal.setText("");
}

private void aktif(boolean x)
{
    txtNoJual.setEnabled(x);
    txtNoJual.setEditable(false);
    
    txtNm_Brg.setEnabled(x);
    txtNm_Brg.setEditable(false);
    
    txtNm_Brg.setEnabled(x);
    txtNm_Brg.setEditable(false);
    
    txtHarga.setEnabled(x);
    txtHarga.setEditable(false);
    
    txtJml.setEnabled(x);
    txtTot.setEnabled(x);
    txtPajak.setEditable(false);
    txtTot.setEditable(false);
    
    txtTotal.setEnabled(x);
    txtTotal.setEditable(false);
    
    txtBayar.setEnabled(x);
    
    txtKembali.setEnabled(x);
    txtKembali.setEditable(false);
    
    txtTot.setEnabled(x);
    txtTotal.setEnabled(x);
    txtId.setEnabled(x);
    
    cmbKd_Kons.setEnabled(x);
    cmbKd_Brg.setEnabled(x);
    txtTgl.setEnabled(x);
    txtJml.setEditable(x);
}

//method set tombol on/off
private void setTombol(boolean t)
{
    cmdTambah.setEnabled(t);
    cmdSimpan.setEnabled(!t);
    cmdBatal.setEnabled(!t);
    cmdKeluar.setEnabled(t);
    cmdHapusItem.setEnabled(!t);
    btnPilih.setEnabled(!t);
}

//method buat nomor jual otomatis
private void nomor_jual()
{
    try{
        stm=Con.createStatement();
        ResultSet rs=stm.executeQuery("select no_jual from jual");
        int brs=0;

        while(rs.next())
        {
            brs=rs.getRow();
        }
        if(brs==0)
            txtNoJual.setText("1");
        else
        {int nom=brs+1;
            txtNoJual.setText(Integer.toString(nom));
        }
        rs.close();
    }
    catch(SQLException e)
    {
        System.out.println("Error : "+e);
    }
}

//method simpan detail jual di tabel (temporary)
private void simpan_ditabel()
{
    try{
        String tKode=cmbKd_Brg.getSelectedItem().toString();
        String tNama=txtNm_Brg.getText();
        double hrg=Double.parseDouble(txtHarga.getText());
        int jml=Integer.parseInt(txtJml.getText());
        double tot=Double.parseDouble(txtTot.getText());
        double pjk=Double.parseDouble(txtPajak.getText());
        tableModel.addRow(new Object[]{tKode,tNama,hrg,jml,tot,pjk});
        inisialisasi_tabel();
    }
    catch(Exception e)
    {
        System.out.println("Error : "+e);
    }
}

//method simpan transaksi penjualan pada table di MySql
private void simpan_transaksi()
{
    try{
        String xnojual=txtNoJual.getText();
        format_tanggal();
        String xkode=cmbKd_Kons.getSelectedItem().toString();
        String msql="insert into jual values('"+xnojual+"','"+xkode+"','"+tanggal+"')";
        stm.executeUpdate(msql);
        for(int i=0;i<tblJual.getRowCount();i++)
        {
            String xkd=(String)tblJual.getValueAt(i,0);
            double xhrg=(Double)tblJual.getValueAt(i,2);
            int xjml=(Integer)tblJual.getValueAt(i,3);
            double xpjk=(Double)tblJual.getValueAt(i, 5);
            String zsql="insert into djual values('"+xnojual+"','"+xkd+"',"+xhrg+","+xjml+","+xpjk+")";
            stm.executeUpdate(zsql);
        }
        
        JOptionPane.showMessageDialog(null, "Data penjualan berhasil disimpan.");
    }
    catch(SQLException e)
    {
        System.out.println("Error : "+e);
    }
}

//method membuat format tanggal sesuai dengan MySQL
private void format_tanggal()
{
    String DATE_FORMAT = "yyyy-MM-dd";
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
    Calendar c1 = Calendar.getInstance();
    int year=c1.get(Calendar.YEAR);
    int month=c1.get(Calendar.MONTH)+1;
    int day=c1.get(Calendar.DAY_OF_MONTH);
    tanggal=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
}

private class PrintingTask extends SwingWorker<Object, Object> {
  private final MessageFormat headerFormat;
  private final MessageFormat footerFormat;
  private final boolean interactive;
  private volatile boolean complete = false;
  private volatile String message;

  public PrintingTask(MessageFormat header, MessageFormat footer, boolean interactive) {
      this.headerFormat = header;
      this.footerFormat = footer;
      this.interactive = interactive;
  }

  @Override
  protected Object doInBackground() {
      try {
          complete = text.print(headerFormat, footerFormat,
          true, null, null, interactive);
          message = "Printing " + (complete ? "complete" : "canceled");
      } catch (PrinterException ex) {
          message = "Sorry, a printer error occurred";
      } catch (SecurityException ex) {
      message = "Sorry, cannot access the printer due to security reasons";
      }
      return null;
  }
  
  @Override
  protected void done() {
      showMessage(!complete, message);
  }
}

private void showMessage(boolean isError, String message) {
  if (isError) {
      JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  } else {
      JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
  }
}

public void itemTerpilih(){
    frmSelectBarang fDB = new frmSelectBarang();
    fDB.fAB = this;              
    txtId.setText(idBrg);
    cmbKd_Brg.setSelectedItem(idBrg);
    txtNm_Brg.setText(namaBrg);
    txtHarga.setText(hargaBrg);
}

//Menghitung Kembalian
private void hitung_bayar(){
    double xtotal,xbayar, xkembali;

    xtotal=Double.parseDouble(txtTotal.getText());
    xbayar=Double.parseDouble(txtBayar.getText());        
    xkembali=xbayar-xtotal;
    String xkembalixx=Double.toString(xkembali);
    txtKembali.setText(xkembalixx);
}

//kosongi table penjualan
private void kosong_table(){
    DefaultTableModel model = (DefaultTableModel) tblJual.getModel();
    model.setRowCount(0); // Menghapus semua baris dalam tabel
}

//method kosongkan detail jual
    private void kosong_detail()
    {
        txtNm_Brg.setText("");
        txtHarga.setText("");
        txtJml.setText("");
        txtTot.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        cmbKd_Brg = new javax.swing.JComboBox<>();
        txtNm_Brg = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtJml = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJual = new javax.swing.JTable();
        txtTot = new javax.swing.JTextField();
        cmdHapusItem = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNoJual = new javax.swing.JTextField();
        txtTgl = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbKd_Kons = new javax.swing.JComboBox<>();
        cmdBatal = new javax.swing.JButton();
        cmdCetak = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        cmdSimpan = new javax.swing.JButton();
        cmdTambah = new javax.swing.JButton();
        btnPilih = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        txtKembali = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        txtPajak = new javax.swing.JTextField();

        jButton4.setText("Batal");

        jButton5.setText("Cetak");

        jButton6.setText("Keluar");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Total");

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jButton2.setText("Simpan");

        jButton3.setText("Tambah");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmbKd_Brg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKd_Brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKd_BrgActionPerformed(evt);
            }
        });

        txtNm_Brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNm_BrgActionPerformed(evt);
            }
        });

        txtJml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJmlActionPerformed(evt);
            }
        });

        tblJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kd Barang", "Nama Barang", "Harga Barang", "Jumlah", "Total", "Pajak"
            }
        ));
        jScrollPane1.setViewportView(tblJual);

        txtTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotActionPerformed(evt);
            }
        });

        cmdHapusItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdHapusItem.setText("Hapus Item");
        cmdHapusItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusItemActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("No. Jual");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Tgl. Jual");

        txtNoJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoJualActionPerformed(evt);
            }
        });

        txtTgl.setModel(new javax.swing.SpinnerDateModel());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Kode Konsumen");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Nama Konsumen");

        cmbKd_Kons.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbKd_Kons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKd_KonsActionPerformed(evt);
            }
        });

        cmdBatal.setText("Batal");
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });

        cmdCetak.setText("Cetak");
        cmdCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCetakActionPerformed(evt);
            }
        });

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Total");

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });

        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        btnPilih.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPilih.setText("Pilih Barang");
        btnPilih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Bayar");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Kembali");

        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });

        text.setColumns(20);
        text.setRows(5);
        jScrollPane2.setViewportView(text);

        txtPajak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPajakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBayar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtKembali)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoJual, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbKd_Kons, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbKd_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdHapusItem))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnPilih)
                                .addGap(32, 32, 32)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPajak)
                            .addComponent(txtTot, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdTambah)
                .addGap(18, 18, 18)
                .addComponent(cmdSimpan)
                .addGap(18, 18, 18)
                .addComponent(cmdBatal)
                .addGap(18, 18, 18)
                .addComponent(cmdCetak)
                .addGap(18, 18, 18)
                .addComponent(cmdKeluar)
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(cmbKd_Kons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKd_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNm_Brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdHapusItem)
                    .addComponent(btnPilih)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPajak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdTambah)
                    .addComponent(cmdSimpan)
                    .addComponent(cmdBatal)
                    .addComponent(cmdCetak)
                    .addComponent(cmdKeluar))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdHapusItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusItemActionPerformed
        try {
        int row = tblJual.getSelectedRow(); // Mendapatkan baris yang dipilih

        if (row != -1) { // Memastikan ada baris yang dipilih
            tableModel.removeRow(row); // Menghapus baris dari tableModel
            inisialisasi_tabel(); // Memperbarui tampilan tabel
        } else {
            JOptionPane.showMessageDialog(null, "Pilih baris yang ingin dihapus");
        }
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
    }//GEN-LAST:event_cmdHapusItemActionPerformed

    private void txtNoJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoJualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoJualActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void cmbKd_BrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_BrgActionPerformed
        String kdBrg = cmbKd_Brg.getSelectedItem().toString();
        detail_barang(kdBrg);
    }//GEN-LAST:event_cmbKd_BrgActionPerformed

    private void txtNm_BrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNm_BrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNm_BrgActionPerformed

    private void cmbKd_KonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKd_KonsActionPerformed
        String kdKons = cmbKd_Kons.getSelectedItem().toString();
        detail_konsumen(kdKons);
    }//GEN-LAST:event_cmbKd_KonsActionPerformed

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        aktif(true);
        setTombol(false);
        baca_barang();
        baca_konsumen();
        kosong();
        kosong_detail();
        kosong_table();
        nomor_jual();
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        simpan_transaksi();
        inisialisasi_tabel();
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        aktif(false);
        setTombol(true);
        kosong();
        kosong_detail();
        kosong_table();
        text.setText("");
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        // System.exit(0);
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void btnPilihActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihActionPerformed
        frmSelectBarang fDB = new frmSelectBarang();
        fDB.fAB = this;
        fDB.setVisible(true);
        fDB.setResizable(false);
    }//GEN-LAST:event_btnPilihActionPerformed

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        format_tanggal();
      String ctk="Nota Penjualan\nNo:"+txtNoJual.getText()+"\nTanggal : "+tanggal;
      ctk=ctk+"\n"+"--------------------------------------------------------------------------------------------------------------------------------";
      ctk=ctk+"\n"+"Kode\tNama Barang\t\tHarga\tJml\tTotal\tPajak";
      ctk=ctk+"\n"+"--------------------------------------------------------------------------------------------------------------------------------";

      for(int i=0;i<tblJual.getRowCount();i++)
      {
          String xkd=(String)tblJual.getValueAt(i,0);
          String xnama=(String)tblJual.getValueAt(i,1);
          double xhrg=(Double)tblJual.getValueAt(i,2);
          int xjml=(Integer)tblJual.getValueAt(i,3);
          double xtot=(Double)tblJual.getValueAt(i,4);
          double xpjk=(Double)tblJual.getValueAt(i, 5);
          ctk=ctk+"\n"+xkd+"\t"+xnama+"\t"+xhrg+"\t"+xjml+"\t"+xtot+"\t"+xpjk;
      }

      ctk=ctk+"\n"+"--------------------------------------------------------------------------------------------------------------------------------";
      ctk=ctk+"\n\t\t\t\t\t"+txtTotal.getText();
      text.setText(ctk);

      String headerField="";
      String footerField="";
      MessageFormat header = new MessageFormat(headerField);
      MessageFormat footer = new MessageFormat(footerField);
      boolean interactive = true;//interactiveCheck.isSelected();
      boolean background = true;//backgroundCheck.isSelected();
      PrintingTask task = new PrintingTask(header, footer, interactive);

      if (background) {
          task.execute();
      } else {
          task.run();
      }
    }//GEN-LAST:event_cmdCetakActionPerformed

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        hitung_bayar();
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtJmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJmlActionPerformed
        hitung_jual();
        simpan_ditabel();
    }//GEN-LAST:event_txtJmlActionPerformed

    private void txtPajakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPajakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPajakActionPerformed

    private void txtTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPilih;
    private javax.swing.JComboBox<String> cmbKd_Brg;
    private javax.swing.JComboBox<String> cmbKd_Kons;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdHapusItem;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTable tblJual;
    private javax.swing.JTextArea text;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtJml;
    private javax.swing.JTextField txtKembali;
    private javax.swing.JTextField txtNm_Brg;
    private javax.swing.JTextField txtNoJual;
    private javax.swing.JTextField txtPajak;
    private javax.swing.JSpinner txtTgl;
    private javax.swing.JTextField txtTot;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
