/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms1;

import dbconnect.dbconnect;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author PRP DOC
 */
public class bill extends javax.swing.JFrame {
    
    
    double num,ans;
    int calculation;
    
    Connection sos = null;
    PreparedStatement sss = null;
    ResultSet rs = null;
    String sqr;
    
    Double totalAmount=0.0;
    Double cash=0.0;
    Double balance=0.0;
    Double bHeight=0.0;
    int i;
    
    ArrayList<String> itemName = new ArrayList<>();
    ArrayList<String> quantity1 = new ArrayList<>();
    ArrayList<String> itemPrice1 = new ArrayList<>();
    ArrayList<String> type1 = new ArrayList<>();
    ArrayList<String> subtotal1 = new ArrayList<>();
    Date date = new Date();
    String iname;
    String itype;
    String itemid;
    String cust_id;
    int iquantity;
    Double iprice;
    Double iamount;
    String ibillno;
    double num2;
    String name1;
    
    public bill(String name) {
    	
    	this.name1=name;
        initComponents();
        sos = dbconnect.connect();
        autoID();
        tablenew();
       
    }
    public void autoID() {
        try {
            

            // Use PreparedStatement to avoid SQL injection
            String query = "SELECT MAX(cust_id) FROM sale";
            sss = sos.prepareStatement(query);
            rs = sss.executeQuery();

            if (rs.next()) {
                String maxCarNo = rs.getString("MAX(cust_id)");

                if (maxCarNo == null) {
                	billnobox.setText("C0001");
                } else {
                    int id = Integer.parseInt(maxCarNo.substring(1));
                    id++;
                    billnobox.setText("C" + String.format("%04d", id));
                }
            }

            
        } catch ( SQLException ex) {
            Logger.getLogger(bill.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        
        // Set the text in the custidbox field
        
  

     public void equal()
            
    {
        switch (calculation)
            
        {
        	case 1 :
        		num2=Double.parseDouble(screen.getText());
        		ans = num + num2 ;
        		screen.setText(Double.toString(ans));
        		remain.setText(Double.toString(num)+"+"+num2);
        		break;
        	case 2 :
        		num2=Double.parseDouble(screen.getText());
        		ans = num - num2 ;
        		screen.setText(Double.toString(ans));
        		remain.setText(Double.toString(num)+"-"+num2);
        		break;
        	case 3 :
        		num2=Double.parseDouble(screen.getText());
        		ans = num / num2 ;
        		screen.setText(Double.toString(ans));
        		remain.setText(Double.toString(num)+"/"+num2);
        		break;
            case 4 :
            	num2=Double.parseDouble(screen.getText());
        		ans = num * num2 ;
        		screen.setText(Double.toString(ans));
        		remain.setText(Double.toString(num)+"*"+num2);
        		break;
          
        }
    }
    
    public void tablenew()
        {
            try 
            {
                String sql = "SELECT fname AS Name,ftype AS Type ,fprice AS Price ,fid as ID FROM food ";
                sss = sos.prepareStatement(sql);
                rs = sss.executeQuery();
                jTable1.setModel(DbUtils.resultSetToTableModel(rs));
            } 
            catch (Exception e) 
            {
            }
     }
       
    public void tabledata()
        {
            int r = jTable1.getSelectedRow();
            
            String name = jTable1.getValueAt(r,0).toString();
            String type = jTable1.getValueAt(r,1).toString();
            String price = jTable1.getValueAt(r,2).toString();
            String id = jTable1.getValueAt(r, 3).toString();
            		
            namebox.setText(name);
            typebox.setText(type);
            pricebox.setText(price);
            itemid = id; 
            
        }
    public void calc() {
    	
    	try{
    		String price = pricebox.getText().toString();
        	String qty = (String)jComboBox1.getSelectedItem();
        	double amt = Double.parseDouble(price)*Double.parseDouble(qty);
        	endprice.setText(amt+"");    	
        	totalAmount = totalAmount+ Double.valueOf(endprice.getText());
        	totalamountbox.setText(""+totalAmount);
        	
        	String cash = cashbox.getText().toString();
    	if(cash==" ") {
    		balancebox.setText(" ");
    	}
    	else {
    		double bal = Double.parseDouble(cash)-Double.parseDouble(totalamountbox.getText());
    		balancebox.setText(""+bal);
    		try {
                String qr= "INSERT INTO `cash`(`bill_no`,`total_amount`, `cash`, `balance`) VALUES ('"+billnobox.getText()+"'"+totalamountbox.getText()+"','"+cashbox.getText()+"','"+balancebox.getText()+"')";
                sss=sos.prepareStatement(qr);
                sss.execute();
     
            } catch (Exception e) {
                
                JOptionPane.showMessageDialog(rootPane, e);
            }
    	}}
    	catch(Exception e) {
    		
    	}
    	 
    	
    }
        public void search()
        {
            String nnn = searchbox.getText();
            
            try 
            {
             String sql = "SELECT fname AS Name,ftype AS Type ,fprice AS Price ,fid as ID  FROM food WHERE fname LIKE '%"+nnn+"%'OR fid LIKE '%"+nnn+"%'";  
             sss = sos.prepareStatement(sql);
             rs = sss.executeQuery();
             jTable1.setModel(DbUtils.resultSetToTableModel(rs));
            }
            catch (Exception e) 
            {
            JOptionPane.showMessageDialog(null,"Error");
            }
            
        }       
        
        public void clear()
    {
     searchbox.setText("");
     namebox.setText ("");
     typebox.setText("");
     pricebox.setText("");
     endprice.setText("");
     
    }
       
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButtoncalc = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        searchbox = new javax.swing.JTextField();
        billnobox = new javax.swing.JLabel();
        bill = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        namebox = new javax.swing.JTextField();
        Type = new javax.swing.JLabel();
        typebox = new javax.swing.JTextField();
        Price = new javax.swing.JLabel();
        pricebox = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        endprice = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        balancebox = new javax.swing.JTextField();
        cashbox = new javax.swing.JTextField();
        totalamountbox = new javax.swing.JTextField();
        subtotalbox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        datebox =new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        seven = new javax.swing.JButton();
        eight = new javax.swing.JButton();
        nine = new javax.swing.JButton();
        six = new javax.swing.JButton();
        five = new javax.swing.JButton();
        three = new javax.swing.JButton();
        two = new javax.swing.JButton();
        one = new javax.swing.JButton();
        zero = new javax.swing.JButton();
        dot = new javax.swing.JButton();
        divide = new javax.swing.JButton();
        multi = new javax.swing.JButton();
        minus = new javax.swing.JButton();
        add = new javax.swing.JButton();
        equals = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        four = new javax.swing.JButton();
        screen = new javax.swing.JTextField();
        remain = new javax.swing.JLabel();
        jButtonad = new javax.swing.JButton();
        custid = new javax.swing.JLabel();
        custidbox = new javax.swing.JLabel();
        jLabelsub =new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Printing Bill");

        jPanel4.setLayout(null);

        jButton4.setText("EXIT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton4);
        jButton4.setBounds(370, 470, 80, 40);

        jButton5.setBackground(new java.awt.Color(51, 0, 51));
        jButton5.setText("BACK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton5);
        jButton5.setBounds(70, 470, 80, 40);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3","Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel4.add(jScrollPane1);
        jScrollPane1.setBounds(70, 60, 390, 300);

        jLabel3.setText("Search");
        jPanel4.add(jLabel3);
        jLabel3.setBounds(210, 10, 60, 30);

        searchbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchboxKeyReleased(evt);
            }
        });
        jPanel4.add(searchbox);
        searchbox.setBounds(280, 10, 180, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Print Bill");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(10, 10, 100, 20);

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3);
        jButton3.setBounds(510, 10, 130, 40);
        
        jButtonad.setText("Add item");
        jButtonad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonadActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonad);
        jButtonad.setBounds(650, 10, 130, 40);
        
        jButtoncalc.setText("Calculate");
        jButtoncalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncalcActionPerformed(evt);
            }
        });
        jPanel3.add(jButtoncalc);
        jButtoncalc.setBounds(345, 175, 100, 35);
        
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        
        bill.setText("Bill no :");
        billnobox.setFont(new Font("",1,15));
        
        jLabel4.setText("Name");

        Type.setText("Type");

        Price.setText("Price of the item");

        jLabel7.setText("Quantity");

        jLabel8.setText("Sub Total");
        
        
        
        jLabel5.setText("Total Amount ");

        jLabel6.setText("Cash");

        jLabel9.setText("balance");

        jLabel1.setText("Date");
        
        datebox.setFont(new Font("",1,15));// NOI18N
        SimpleDateFormat sdf = new SimpleDateFormat();
        String dt = sdf.format(date);
        datebox.setText(dt);
        
        
        

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                		.addComponent(bill)
                    .addComponent(jLabel1)
                    .addComponent(Price)
                    .addComponent(Type)
                    .addComponent(jLabel4)
                    
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    		.addComponent(billnobox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(pricebox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(typebox, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(namebox, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(endprice))
                    .addComponent(datebox)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                	
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                		
                    .addComponent(totalamountbox)
                    .addComponent(cashbox)
                    .addComponent(balancebox, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                		.addComponent(bill)
                        .addComponent(billnobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(namebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Type, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pricebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Price))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(datebox))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                   
                    .addComponent(jLabel5)
                    .addComponent(totalamountbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cashbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(balancebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );

        jPanel4.add(jPanel3);
        jPanel3.setBounds(510, 60, 460, 390);

        jButton7.setText("Print");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton7);
        jButton7.setBounds(610, 460, 280, 50);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/otherim.jpg"))); // NOI18N
        jLabel10.setText("jLabel10");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(0, 0, 1000, 530);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));
        jPanel5.setForeground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Calculator");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, -1));

        seven.setBackground(new java.awt.Color(255, 255, 255));
        seven.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        seven.setText("7");
        seven.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        seven.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sevenActionPerformed(evt);
            }
        });
        jPanel5.add(seven, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 50, 30));

        eight.setBackground(new java.awt.Color(255, 255, 255));
        eight.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        eight.setText("8");
        eight.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        eight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eightActionPerformed(evt);
            }
        });
        jPanel5.add(eight, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 50, 30));

        nine.setBackground(new java.awt.Color(255, 255, 255));
        nine.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        nine.setText("9");
        nine.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        nine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nineActionPerformed(evt);
            }
        });
        jPanel5.add(nine, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 50, 30));

        six.setBackground(new java.awt.Color(255, 255, 255));
        six.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        six.setText("6");
        six.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        six.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sixActionPerformed(evt);
            }
        });
        jPanel5.add(six, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 50, 30));

        five.setBackground(new java.awt.Color(255, 255, 255));
        five.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        five.setText("5");
        five.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        five.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveActionPerformed(evt);
            }
        });
        jPanel5.add(five, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 50, 30));

        three.setBackground(new java.awt.Color(255, 255, 255));
        three.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        three.setText("3");
        three.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        three.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                threeActionPerformed(evt);
            }
        });
        jPanel5.add(three, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 50, 30));

        two.setBackground(new java.awt.Color(255, 255, 255));
        two.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        two.setText("2");
        two.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        two.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twoActionPerformed(evt);
            }
        });
        jPanel5.add(two, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 50, 30));

        one.setBackground(new java.awt.Color(255, 255, 255));
        one.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        one.setText("1");
        one.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        one.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneActionPerformed(evt);
            }
        });
        jPanel5.add(one, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 50, 30));

        zero.setBackground(new java.awt.Color(255, 255, 255));
        zero.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        zero.setText("0");
        zero.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        zero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zeroActionPerformed(evt);
            }
        });
        jPanel5.add(zero, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 50, 30));

        dot.setBackground(new java.awt.Color(255, 255, 255));
        dot.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        dot.setText(".");
        dot.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        dot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dotActionPerformed(evt);
            }
        });
        jPanel5.add(dot, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, 50, 30));

        divide.setBackground(new java.awt.Color(255, 255, 255));
        divide.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        divide.setText("/");
        divide.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        divide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                divideActionPerformed(evt);
            }
        });
        jPanel5.add(divide, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 50, 30));

        multi.setBackground(new java.awt.Color(255, 255, 255));
        multi.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        multi.setText("*");
        multi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        multi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiActionPerformed(evt);
            }
        });
        jPanel5.add(multi, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 50, 30));

        minus.setBackground(new java.awt.Color(255, 255, 255));
        minus.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        minus.setText("-");
        minus.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minusActionPerformed(evt);
            }
        });
        jPanel5.add(minus, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 50, 30));

        add.setBackground(new java.awt.Color(255, 255, 255));
        add.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        add.setText("+");
        add.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        jPanel5.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 110, 30));

        equals.setBackground(new java.awt.Color(255, 255, 255));
        equals.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        equals.setText("=");
        equals.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        equals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equalsActionPerformed(evt);
            }
        });
        jPanel5.add(equals, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 170, 30));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        jButton1.setText("Clear");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 230, 20));

        four.setBackground(new java.awt.Color(255, 255, 255));
        four.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        four.setText("4");
        four.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        four.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fourActionPerformed(evt);
            }
        });
        jPanel5.add(four, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 50, 30));

        screen.setEditable(false);
        screen.setBackground(new java.awt.Color(0, 0, 0));
        screen.setFont(new java.awt.Font("Tw Cen MT", 3, 14)); // NOI18N
        screen.setForeground(new java.awt.Color(255, 255, 255));
        screen.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        screen.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        screen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                screenActionPerformed(evt);
            }
        });
        jPanel5.add(screen, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 230, 30));

        remain.setFont(new java.awt.Font("Tw Cen MT", 3, 12)); // NOI18N
        remain.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(remain, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 80, 10));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
            tabledata();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
     
    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void searchboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchboxKeyReleased
        search();
    }//GEN-LAST:event_searchboxKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        int check = JOptionPane.showConfirmDialog(null,"Do you want to exit","Warning",JOptionPane.YES_NO_OPTION);
        if (check == 0)
        {System.exit(0);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        
    }//GEN-LAST:event_jTable1KeyPressed
    private void jButtoncalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       calc(); 
    }
    
    private void jButtonadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    	 iname = namebox.getText();
    	 ibillno = billnobox.getText();
         iquantity = Integer.parseInt(jComboBox1.getSelectedItem().toString());
         iprice = Double.valueOf(pricebox.getText());
         iamount = Double.valueOf(endprice.getText());
         
         
         
         
         
         try {
             String qr= "INSERT INTO `sale`(`cust_id`,`item_name`, `quantity`, `item_price`, `amount`,`fid`) VALUES ('"+ibillno+"','"+iname+"','"+iquantity+"','"+iprice+"','"+iamount+"','"+itemid+"')";
              sss = sos.prepareStatement(qr);
              sss.execute();
  
         } catch (Exception e) {
             
             JOptionPane.showMessageDialog(rootPane, e);
         }
         clear();
    }
    
    


	


	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed         
         getData();
         getBillData();
         bHeight = Double.valueOf(itemName.size());
         int r=itemName.size();
         //JOptionPane.showMessageDialog(rootPane, bHeight);

         PrinterJob pj = PrinterJob.getPrinterJob();
         pj.setPrintable(new BillPrintable(),getPageFormat(pj));
         try {
             pj.print();

         }
         catch (PrinterException ex) {
             ex.printStackTrace();
         }

        for (int i =0;i<r;i++) {	
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String dt = sdf.format(date);
        
         try 
         {
             String sen = "INSERT INTO allsale (sname ,sprice,squantity,sendprice,stotalamou,scashs,sbalances,date,Eid) VALUES ('"+itemName.get(i)+"','"+itemPrice1.get(i)+"','"+quantity1.get(i)+"','"+subtotal1.get(i)+"','"+totalamountbox.getText()+"','"+cash+"','"+balance+"','"+dt+"','"+name1+"')";
             sss = sos.prepareStatement(sen);
             sss.execute();
             
             
         } catch (Exception e) 
         {
           JOptionPane.showMessageDialog(null,e);
         }                                
        }
        JOptionPane.showMessageDialog(null,"Succesful");
        autoID();
        }
        
        
        
//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    	try
        {
        	String sql = "SELECT * FROM manregis WHERE midnumber=?";
            sss = sos.prepareStatement(sql);
            sss.setString(1,name1);
            rs=sss.executeQuery();
            if(rs.next()) {
                afterlog sy = new afterlog (name1);
                sy.setVisible(true);
                this.dispose();
            }else {
            	afteremp sy = new afteremp (name1);
                sy.setVisible(true);
                this.dispose();
            }
        } catch (Exception e)
        {
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void sevenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sevenActionPerformed
        screen.setText(screen.getText()+ "7");;
    }//GEN-LAST:event_sevenActionPerformed

    private void eightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eightActionPerformed
        screen.setText(screen.getText()+ "8");
    }//GEN-LAST:event_eightActionPerformed

    private void nineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nineActionPerformed
        screen.setText(screen.getText()+ "9");
    }//GEN-LAST:event_nineActionPerformed

    private void sixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sixActionPerformed
        screen.setText(screen.getText()+ "6");
    }//GEN-LAST:event_sixActionPerformed

    private void fiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveActionPerformed
        screen.setText(screen.getText()+ "5");
    }//GEN-LAST:event_fiveActionPerformed

    private void threeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_threeActionPerformed
        screen.setText(screen.getText()+ "3");
    }//GEN-LAST:event_threeActionPerformed

    private void twoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twoActionPerformed
        screen.setText(screen.getText()+ "2");
    }//GEN-LAST:event_twoActionPerformed

    private void oneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneActionPerformed
        screen.setText(screen.getText()+ "1");
    }//GEN-LAST:event_oneActionPerformed

    private void zeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zeroActionPerformed
        screen.setText(screen.getText()+ "0");
    }//GEN-LAST:event_zeroActionPerformed

    private void dotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dotActionPerformed
        screen.setText(screen.getText()+ ".");
    }//GEN-LAST:event_dotActionPerformed

    private void equalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equalsActionPerformed
        equal();
    }//GEN-LAST:event_equalsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        screen.setText(" ");
        remain.setText(" ");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void fourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fourActionPerformed
        screen.setText(screen.getText()+ "4");
    }//GEN-LAST:event_fourActionPerformed

    private void screenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_screenActionPerformed

    }//GEN-LAST:event_screenActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        num = Double.parseDouble(screen.getText());
        calculation = 1;
        screen.setText(" ");
        remain.setText(num+"+");
    }//GEN-LAST:event_addActionPerformed

    private void minusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minusActionPerformed
        num = Double.parseDouble(screen.getText());
        calculation = 2;
        screen.setText(" ");
        remain.setText(num+"-");
    }//GEN-LAST:event_minusActionPerformed
    
    private void divideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_divideActionPerformed
        num = Double.parseDouble(screen.getText());
        calculation = 3;
        screen.setText(" ");
        remain.setText(num+"/");
    }//GEN-LAST:event_divideActionPerformed

    private void multiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiActionPerformed
        num = Double.parseDouble(screen.getText());
        calculation = 4;
        screen.setText(" ");
        remain.setText(num+"*");
    }//GEN-LAST:event_multiActionPerformed

    
     private void getData()
    {
    
        try {
            String sql="SELECT `cust_id`, `item_name`,`quantity`, `item_price`, `amount` FROM `sale` WHERE cust_id='"+billnobox.getText()+"'";
            sss = sos.prepareStatement(sql);
             rs=sss.executeQuery();
             while(rs.next())
             {
                 itemName.add(rs.getString("item_name"));
                 quantity1.add(rs.getString("quantity"));
                 itemPrice1.add(rs.getString("item_price"));
                 subtotal1.add(rs.getString("amount"));
          
                }
        } catch (Exception e) {
        }
    }
     
    private void getBillData()
    {
    
        try {
            String sql="SELECT `bill_no`, `total_amount`, `cash`, `balance` FROM `cash` WHERE bill_no='"+billnobox.getText()+"'";
        sss = sos.prepareStatement(sql);
             rs= sss.executeQuery();
             while(rs.next())
             {
                 totalAmount = rs.getDouble("total_amount");
                 cash = rs.getDouble("cash");
                 
                 balance = rs.getDouble("balance");
                 
                }
        } catch (Exception e) {
        }
    }
        
    public PageFormat getPageFormat(PrinterJob pj)
{
    
    PageFormat pf = pj.defaultPage();
    Paper paper = pf.getPaper();    

    double bodyHeight = bHeight;  
    double headerHeight = 5.0;                  
    double footerHeight = 5.0;        
    double width = cm_to_pp(8); 
    double height = cm_to_pp(headerHeight+bodyHeight+footerHeight); 
    paper.setSize(width, height);
    paper.setImageableArea(0,10,width,height - cm_to_pp(1));  
            
    pf.setOrientation(PageFormat.PORTRAIT);  
    pf.setPaper(paper);    

    return pf;
}
   
    
    
    protected static double cm_to_pp(double cm)
    {            
	        return toPPI(cm * 0.393600787);            
    }
 
protected static double toPPI(double inch)
    {            
	        return inch * 72d;            
    }
    


public class BillPrintable implements Printable {
    
   
    
    
  public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
  throws PrinterException 
  {    
      
      int r= itemName.size();
      ImageIcon icon=new ImageIcon("C:UsersccsDocumentsNetBeansProjectsvideo TestPOSInvoicesrcposinvoicemylogo.jpg"); 
      int result = NO_SUCH_PAGE;    
        if (pageIndex == 0) {                    
        
            Graphics2D g2d = (Graphics2D) graphics;                    
            double width = pageFormat.getImageableWidth();                               
            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 



          //  FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
        
        try{
            int y=22;
            int yShift = 10;
            int headerRectHeight=15;
           // int headerRectHeighta=40;
            
                
            g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
            g2d.drawImage(icon.getImage(), 50, 20, 90, 30, rootPane);y+=yShift+30;
            g2d.drawString("-------------------------------------",12,y);y+=yShift;
            g2d.drawString("        Foodie court        ",12,y);y+=yShift;
            g2d.drawString("   Lane 2, Balmatta Function  ",12,y);y+=yShift;
            g2d.drawString("        MANGALURU , DK   ",12,y);y+=yShift;
            g2d.drawString("   Restaurant Bill Provide  ",12,y);y+=yShift;
            g2d.drawString("        +94772623631      ",12,y);y+=yShift;
            g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;

            g2d.drawString(" Item Name                   Price   ",10,y);y+=yShift;
            g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
            for(int i=0;i<r;i++) {
            
            g2d.drawString(" "+itemName.get(i)+"                                ",10,y);y+=yShift;
            g2d.drawString("      "+Integer.parseInt(quantity1.get(i).toString())+" * "+itemPrice1.get(i),10,y); g2d.drawString(subtotal1.get(i),160,y);y+=yShift;

         
            }
            g2d.drawString("-------------------------------------",10,y);y+=yShift;
            g2d.drawString(" Total amount:               "+totalamountbox.getText()+"   ",10,y);y+=yShift;
            g2d.drawString("-------------------------------------",10,y);y+=yShift;
            g2d.drawString(" Cash      :                 "+cashbox.getText()+"   ",10,y);y+=yShift;
            g2d.drawString("-------------------------------------",10,y);y+=yShift;
            g2d.drawString(" Balance   :                 "+balancebox.getText()+"   ",10,y);y+=yShift;
  
            g2d.drawString("*************************************",10,y);y+=yShift;
            g2d.drawString("       THANK YOU COME AGAIN            ",10,y);y+=yShift;
            g2d.drawString("*************************************",10,y);y+=yShift;
            g2d.drawString("  SOFTWARE BY:PRP Documentations   ",10,y);y+=yShift;
            g2d.drawString("   CONTACT: PRPdoc62@gmail.com      ",10,y);y+=yShift;       
           

    }
    catch(Exception e){
    e.printStackTrace();
    }

              result = PAGE_EXISTS;    
          }    
          return result;    
      }
   }
    
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
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	
                new bill("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Price;
    private javax.swing.JLabel Type;
    private javax.swing.JButton add;
    private javax.swing.JTextField balancebox;
    private javax.swing.JTextField cashbox;
    private javax.swing.JButton divide;
    private javax.swing.JButton dot;
    private javax.swing.JButton eight;
    private javax.swing.JTextField endprice;
    private javax.swing.JButton equals;
    private javax.swing.JButton five;
    private javax.swing.JButton four;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelsub;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton minus;
    private javax.swing.JButton multi;
    private javax.swing.JTextField namebox;
    private javax.swing.JButton nine;
    private javax.swing.JButton one;
    private javax.swing.JTextField pricebox;
    private javax.swing.JLabel remain;
    private javax.swing.JTextField screen;
    private javax.swing.JTextField searchbox;
    private javax.swing.JButton seven;
    private javax.swing.JButton six;
    private javax.swing.JButton three;
    private javax.swing.JTextField totalamountbox;
    private javax.swing.JTextField subtotalbox;
    private javax.swing.JButton two;
    private javax.swing.JTextField typebox;
    private javax.swing.JButton zero;
    private javax.swing.JLabel datebox;
    private javax.swing.JButton jButtoncalc;
    private javax.swing.JButton jButtonad;
    private javax.swing.JLabel custid;
    private javax.swing.JLabel custidbox;
    private javax.swing.JLabel billnobox;
    private javax.swing.JLabel bill;
    // End of variables declaration//GEN-END:variables
}
