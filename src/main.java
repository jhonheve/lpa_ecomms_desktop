import java.awt.*;
import java.awt.event.*;
import java.io.IOException;     
import java.net.URL;
import java.sql.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.util.regex.*;


@SuppressWarnings("all")
public class main extends JFrame {

    /**  
     * Declaring constants variables
     * */
    private static final String MySQL_DB = "lpa_ecomms";
    public static final String user      = "root";
    public static final String pass      = ""; 
    public static final String url       = "jdbc:mysql://localhost:3306/" + MySQL_DB;
	
    /** 
     * Declaring global variables
     */
    public String DisplayName = "";
    public JDesktopPane contentPane;
    public ImageIcon mainIcon,stockIcon,salesIcon,invoiceIcon,clientIcon,
                     adminIcon,exitIcon,usersIcon,helpIcon,aboutIcon,secIcon,
                     keysIcon,loginBGIcon,searchIcon;
	public JMenu lpa_mnSystemAdmin;
	public JMenuItem lpa_mntmMyUserDetails;
    public JSeparator mnMenuSep_1,mnMenuSep_2;
    public Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
    public JMenuBar lpa_menuBar;
    public JInternalFrame ifLogin,ifSearchStock,ifStock,ifSales,ifSearchUsers,ifUsers,ifNewInvoice,ifSearchClients,ifEditClient,ifHelpGuide,ifAbout;
    public JLayeredPane layeredPaneBG,layeredPaneFG;
    public JScrollPane searchScrollPaneStock,searchScrollPaneUsers,searchScrollPaneSales,newInvoiceScrollPane,searchScrollPaneClients;
    public JTable tblSearchStock,tblSearchUsers,tblSearchSales,tblInvoicedItems,tblSearchClients;
    public DefaultTableModel stockModel,usersModel,invoicesModel,invoicedTtems,clientsModel;
    public JTextField txtUsername,txtClientsSearch;
    public JPasswordField txtPassword,txtEditUserPassword;
    public JComboBox<String> cboxUserGroup = new JComboBox(UserGroups.values());
    public JButton btnDeleteUser,btnDeleteStock,btnDeleteClient, btnNewClients, btnNewSales,btnNewStock;
    public Connection con;
    public Statement st;
    public JLabel lblDisplayName = new JLabel();
    public JTextField txtStockSearch,
    					txtStockID,txtStockName,txtStockDes,
    					txtUsersSearch,txtEditUsername,txtUserID,
    					txtUserFirstName,txtUserLastName,txtSalesSearch,
    					txtClientIDSearch,txtCliendId,txtName,
    					txtPhone,txtItemID,txtNameItem,txtPrice,
    					txtQuantity,txtStockOnHand,txtStockPrice;					
    JRadioButton rdbtnActive,rdbtnInactive;

    public String saveMode;
    private JTextField textField,txtEditClientID,txtClientFirstName,txtClientLastName,txtClientAddress,txtClientPhone;
    private JTextField txtFirstname;
    private JTextField txtClientid;
    private JTextField txtLastname;
    private JTextField txtAddress;
    private JTextField txtInvStockID;
    //variable to keep the amount of a new invoice
    private Float totalAmountInvoice = 0F;
    //variable to control access to admin options (edition)
    private boolean admin = false;
    //Current User ID
    private String currentUserID = "";
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					main frame = new main();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 *  
	 */
	public main() {
		setTitle("LPA - Administration System v1.0");
		mainIcon = new ImageIcon("ext-lib/LPALogo.png");
		setIconImage(mainIcon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 971, 614);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		lpa_menuBar = new JMenuBar();
		setJMenuBar(lpa_menuBar);
		
		JMenu lpa_mnMenu = new JMenu("Menu");
		lpa_menuBar.add(lpa_mnMenu);
		
		JMenuItem lpa_mntmStockControl = new JMenuItem("Stock Management");
		stockIcon = new ImageIcon("ext-lib/stockIcon.png");
		lpa_mntmStockControl.setIcon(stockIcon);
		lpa_mntmStockControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//centerJIF(ifSearchStock,"app");
				ifSearchStock.setVisible(true);
;			}
		});
		lpa_mnMenu.add(lpa_mntmStockControl);
		
		JMenu lpa_mnSalesInvoicing = new JMenu("Sales and Invoicing");
		salesIcon = new ImageIcon("ext-lib/salesIcon.png");
		lpa_mnSalesInvoicing.setIcon(salesIcon);
		lpa_mnMenu.add(lpa_mnSalesInvoicing);
		
		JMenuItem lpa_mntmInvoices = new JMenuItem("Invoices");
		lpa_mntmInvoices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSales.setVisible(true);  
			}
		});
		invoiceIcon = new ImageIcon("ext-lib/invoiceIcon.png");
		lpa_mntmInvoices.setIcon(invoiceIcon);
		lpa_mnSalesInvoicing.add(lpa_mntmInvoices);
		
		JMenuItem lpa_mntmClients = new JMenuItem("Clients");
		lpa_mntmClients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSearchClients.setVisible(true);  
			}
		});
		clientIcon = new ImageIcon("ext-lib/clientIcon.png");
		lpa_mntmClients.setIcon(clientIcon);
		lpa_mnSalesInvoicing.add(lpa_mntmClients);
		
		mnMenuSep_1 = new JSeparator();
		lpa_mnMenu.add(mnMenuSep_1);
		
		lpa_mnSystemAdmin = new JMenu("System Administration");
		adminIcon = new ImageIcon("ext-lib/adminIcon.png");
		lpa_mnSystemAdmin.setIcon(adminIcon);
		lpa_mnMenu.add(lpa_mnSystemAdmin);
		lpa_mnSystemAdmin.setVisible(false);
		
		lpa_mntmMyUserDetails = new JMenuItem("My User Details");
		lpa_mntmMyUserDetails.setIcon(adminIcon);
		lpa_mntmMyUserDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getUsersData(currentUserID);
				ifUsers.setVisible(true);
				}
		});
		lpa_mnMenu.add(lpa_mntmMyUserDetails);
		
		JMenuItem lpa_mntmUserMan = new JMenuItem("User Management");
		usersIcon = new ImageIcon("ext-lib/usersIcon.png");
		lpa_mntmUserMan.setIcon(usersIcon);
		lpa_mnSystemAdmin.add(lpa_mntmUserMan);
		
		lpa_mntmUserMan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSearchUsers.setVisible(true);  
			}
		});
		
		mnMenuSep_2 = new JSeparator();
		lpa_mnMenu.add(mnMenuSep_2);
		
		JMenu mnExit = new JMenu("Exit");
		exitIcon = new ImageIcon("ext-lib/exitIcon.png");
		mnExit.setIcon(exitIcon);
		lpa_mnMenu.add(mnExit);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				do_logout();
			}
		});
		ImageIcon logoutIcon = new ImageIcon("ext-lib/logoutIcon.png");
		mntmLogout.setIcon(logoutIcon);
		
		mnExit.add(mntmLogout);
		
		JSeparator separator = new JSeparator();
		mnExit.add(separator);
		
		JMenuItem mntmShutdown = new JMenuItem("Shutdown");
		mntmShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		ImageIcon shutdownIcon = new ImageIcon("ext-lib/shutdownIcon.png");
		mntmShutdown.setIcon(shutdownIcon);
		mnExit.add(mntmShutdown);
		
		
		JMenu lpa_mnHelp = new JMenu("Help");
		lpa_menuBar.add(lpa_mnHelp);
		
		JMenuItem mntmHelpGuide = new JMenuItem("Help Guide");
		helpIcon = new ImageIcon("ext-lib/helpIcon.png");
		mntmHelpGuide.setIcon(helpIcon);
		lpa_mnHelp.add(mntmHelpGuide);
		
		 mntmHelpGuide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifHelpGuide.setVisible(true);
				}
		});
		JMenuItem mntmAboutLpa = new JMenuItem("About LPA");
		aboutIcon = new ImageIcon("ext-lib/aboutIcon.png");
		mntmAboutLpa.setIcon(aboutIcon);
		lpa_mnHelp.add(mntmAboutLpa);
		mntmAboutLpa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifAbout.setVisible(true);
				}
		});

		contentPane = new JDesktopPane() {
			   protected void paintComponent( Graphics g ) {
			    	Graphics2D g2d = (Graphics2D) g;
			        int w = getWidth();
			        int h = getHeight();
			        Color color1 = new Color(0, 0, 40);
			        Color color2 = new Color(80, 80, 100);
			        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
			        g2d.setPaint(gp);
			        g2d.fillRect(0, 0, w, h);
			    }
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ifLogin = new JInternalFrame("LPA - LOGIN");
		ifLogin.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifLogin.setVisible(false);
		ifLogin.getContentPane().setBackground(new Color(204, 204, 255));
		ifLogin.setBounds(117, 44, 390, 211);
		keysIcon = new ImageIcon("ext-lib/iconKey.png");
		
		ifLogin.setFrameIcon(keysIcon);
		centerJIF(ifLogin,"screen");
		contentPane.add(ifLogin);
		ifLogin.getContentPane().setLayout(null);

		layeredPaneBG = new JLayeredPane();
		layeredPaneBG.setBounds(0, 0, 388, 178);
		secIcon = new ImageIcon("ext-lib/securityIcon.png");
		loginBGIcon = new ImageIcon("ext-lib/lpaUserLoginBG.png");
		ifLogin.getContentPane().add(layeredPaneBG);
		layeredPaneFG = new JLayeredPane();
		layeredPaneFG.setBounds(0, 0, 388, 178);
		layeredPaneBG.add(layeredPaneFG);
				
				JLabel lblUserName = new JLabel("User name:");
				lblUserName.setFont(new Font("Tahoma", Font.BOLD, 11));
				lblUserName.setBounds(10, 100, 81, 14);
				layeredPaneFG.add(lblUserName);
				
				txtUsername = new JTextField();
				txtUsername.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_login();
					}
				});
				txtUsername.setBounds(84, 97, 288, 20);
				layeredPaneFG.add(txtUsername);
				txtUsername.setColumns(10);
				
				JLabel lblPassword = new JLabel("Password:");
				lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
				lblPassword.setBounds(10, 125, 64, 14);
				layeredPaneFG.add(lblPassword);
				
				txtPassword = new JPasswordField();
				txtPassword.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						do_login();
					}
				});
				txtPassword.setBounds(85, 122, 287, 20);
				layeredPaneFG.add(txtPassword);
				
				JButton btnLogin = new JButton("Login");
				btnLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//call relevant function();
					}
				});
				btnLogin.setBounds(283, 148, 89, 23);
				layeredPaneFG.add(btnLogin);
				JLabel loginBGLabel = new JLabel();
				loginBGLabel.setVerticalAlignment(SwingConstants.TOP);
				loginBGLabel.setBounds(0, 0, 378, 89);
				layeredPaneFG.add(loginBGLabel);
				loginBGLabel.setIcon(loginBGIcon);
		searchIcon = new ImageIcon("ext-lib/searchIcon.png");
		
		String[] columnNames = {
				"Stock ID",
                "Stock Name",
                "On-Hand",
                "Price"
                };
		Object[][] data = null;
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		/*
		tblSearchStock.getTableHeader().getColumnModel().getColumn(2).setHeaderRenderer(centerRenderer);
		tblSearchStock.getTableHeader().getColumnModel().getColumn(3).setHeaderRenderer(rightRenderer);
		*/
		
		ifStock = new JInternalFrame("LPA - Stock Record");
		ifStock.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifStock.setClosable(true);
		ifStock.setBackground(new Color(35, 44, 49));
		ifStock.setBounds(170, 30, 618, 244);
		contentPane.add(ifStock);
		ifStock.getContentPane().setLayout(null);
		
		JLabel lblStockId = new JLabel("Stock ID:");
		lblStockId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStockId.setForeground(Color.WHITE);
		lblStockId.setBounds(10, 11, 80, 14);
		ifStock.getContentPane().add(lblStockId);
		
		txtStockID = new JTextField();
		txtStockID.setBackground(Color.DARK_GRAY);
		txtStockID.setForeground(Color.WHITE);
		txtStockID.setBounds(100, 9, 203, 20);
		txtStockID.setEditable(false);
		ifStock.getContentPane().add(txtStockID);
		txtStockID.setColumns(10);
		
		JLabel lblStockName = new JLabel("Stock Name:");
		lblStockName.setForeground(Color.WHITE);
		lblStockName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStockName.setBounds(10, 42, 80, 14);
		ifStock.getContentPane().add(lblStockName);
		
		txtStockName = new JTextField();
		txtStockName.setForeground(Color.WHITE);
		txtStockName.setColumns(10);
		txtStockName.setBackground(Color.DARK_GRAY);
		txtStockName.setBounds(100, 40, 492, 20);
		ifStock.getContentPane().add(txtStockName);
		
		txtStockDes = new JTextField();
		txtStockDes.setForeground(Color.WHITE);
		txtStockDes.setColumns(10);
		txtStockDes.setBackground(Color.DARK_GRAY);
		txtStockDes.setBounds(100, 71, 492, 20);
		ifStock.getContentPane().add(txtStockDes);
		
		JLabel lblStockDes = new JLabel("Stock Desc.:");
		lblStockDes.setForeground(Color.WHITE);
		lblStockDes.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStockDes.setBounds(10, 73, 80, 14);
		ifStock.getContentPane().add(lblStockDes);
		
		txtStockOnHand = new JTextField();
		txtStockOnHand.setForeground(Color.WHITE);
		txtStockOnHand.setColumns(10);
		txtStockOnHand.setBackground(Color.DARK_GRAY);
		txtStockOnHand.setBounds(100, 102, 80, 20);
		ifStock.getContentPane().add(txtStockOnHand);
		
		JLabel lblStockOnHand = new JLabel("On-Hand:");
		lblStockOnHand.setForeground(Color.WHITE);
		lblStockOnHand.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStockOnHand.setBounds(10, 104, 80, 14);
		ifStock.getContentPane().add(lblStockOnHand);
		
		txtStockPrice = new JTextField();
		txtStockPrice.setForeground(Color.WHITE);
		txtStockPrice.setColumns(10);
		txtStockPrice.setBackground(Color.DARK_GRAY);
		txtStockPrice.setBounds(100, 133, 80, 20);
		ifStock.getContentPane().add(txtStockPrice);
		
		JLabel lblStockPrice = new JLabel("Price:");
		lblStockPrice.setForeground(Color.WHITE);
		lblStockPrice.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStockPrice.setBounds(10, 135, 80, 14);
		ifStock.getContentPane().add(lblStockPrice);
		
		JButton btnStockSave = new JButton("Save");
		btnStockSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//call relevant function(txtStockID.getText());
			}
		});
		btnStockSave.setForeground(Color.WHITE);
		btnStockSave.setBackground(Color.DARK_GRAY);
		btnStockSave.setBounds(503, 181, 89, 23);
		ifStock.getContentPane().add(btnStockSave);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 170, 582, 2);
		ifStock.getContentPane().add(separator_1);
		
		btnDeleteStock = new JButton("Delete");
		btnDeleteStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//call relevant function(txtStockID.getText());
			}

		});
		btnDeleteStock.setBounds(388, 181, 89, 23);
		ifStock.getContentPane().add(btnDeleteStock);
		
				ifSearchStock = new JInternalFrame("LPA - Search Stock");
				ifSearchStock.setBounds(10, 42, 725, 312);
				contentPane.add(ifSearchStock);
				ifSearchStock.setFrameIcon(searchIcon);
				ifSearchStock.setClosable(true);
				ifSearchStock.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				ifSearchStock.setBackground(new Color(35, 44, 49));
				ifSearchStock.getContentPane().setLayout(null);
				
				JLabel lblSearch = new JLabel("Search:");
				lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
				lblSearch.setForeground(new Color(102, 147, 182));
				lblSearch.setBounds(10, 14, 46, 14);
				ifSearchStock.getContentPane().add(lblSearch);
				
				txtStockSearch = new JTextField();
				txtStockSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchStockData(txtStockSearch.getText().toString());
					}
				});
				txtStockSearch.setForeground(Color.WHITE);
				txtStockSearch.setBackground(Color.DARK_GRAY);
				txtStockSearch.setBounds(66, 11, 534, 20);
				ifSearchStock.getContentPane().add(txtStockSearch);
				txtStockSearch.setColumns(10);
				tblSearchStock = new JTable();
				tblSearchStock.setForeground(Color.WHITE);
				tblSearchStock.setBackground(Color.DARK_GRAY);
				tblSearchStock.setModel(new DefaultTableModel(data,columnNames) {
				    @Override
				    public boolean isCellEditable(int row, int column) {
				       /* Set all cells to NON Editable 
				        *   - change return value to "true" for Editable 
				        * */ 
				       return false;
				    }
          }
				);
				tblSearchStock.addMouseListener(new MouseAdapter() {
				    @Override
				    public void mouseClicked(final MouseEvent e) {
				    	if(admin)
				    	{
					        if (e.getClickCount() == 1) {
					            final JTable target = (JTable)e.getSource();
					            final int row = target.getSelectedRow();
					            final int column = target.getSelectedColumn();
					            String val = target.getValueAt(row, 0).toString();
					            getStockData(val);
					            saveMode="Update";
					            btnDeleteStock.setVisible(true);
					        }
				    	}
				    }
				}); 
				tblSearchStock.getColumnModel().getColumn(1).setPreferredWidth(300);
				tblSearchStock.getColumnModel().getColumn(2).setPreferredWidth(30);
				tblSearchStock.getColumnModel().getColumn(3).setPreferredWidth(50);
				tblSearchStock.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
				tblSearchStock.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
				
				searchScrollPaneStock = new JScrollPane(tblSearchStock);
				searchScrollPaneStock.setBounds(10, 47, 689, 191);
				tblSearchStock.setFillsViewportHeight(true);		
				ifSearchStock.getContentPane().add(searchScrollPaneStock);
				
				JButton btnClose = new JButton("Close");
				btnClose.setForeground(Color.WHITE);
				btnClose.setBackground(Color.DARK_GRAY);
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ifSearchStock.setVisible(false);
					}
				});
				btnClose.setBounds(610, 249, 89, 23);
				ifSearchStock.getContentPane().add(btnClose);
				
						btnNewStock = new JButton("New");
						btnNewStock.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								txtStockID.setText(new Integer(genID()).toString());
								txtStockName.setText("");
					        	txtStockDes.setText("");
					        	txtStockOnHand.setText("");
					        	txtStockPrice.setText("");
								saveMode="new";
								centerJIF(ifStock,"app");
								btnDeleteStock.setVisible(false);
							}
						});
						btnNewStock.setForeground(Color.WHITE);
						btnNewStock.setBackground(Color.DARK_GRAY);
						btnNewStock.setBounds(511, 249, 89, 23);
						
						JButton btnSearchStock = new JButton("Search");
						btnSearchStock.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								searchStockData(txtStockSearch.getText().toString());
							}
						});
						tblSearchStock.getTableHeader().setForeground(Color.WHITE);
						tblSearchStock.getTableHeader().setBackground(Color.DARK_GRAY);
						
						tblSearchStock.getTableHeader().setReorderingAllowed(false);
						btnSearchStock.setForeground(Color.WHITE);
						btnSearchStock.setBackground(Color.DARK_GRAY);
						btnSearchStock.setBounds(610, 11, 89, 23);
						ifSearchStock.getContentPane().add(btnSearchStock);
						
						ifSearchStock.getContentPane().add(btnNewStock);
						ifSearchStock.setVisible(false);
		ifStock.setVisible(false);		
				buildUserEditPanel();
				showUsersPanel(rightRenderer, centerRenderer);
				buildNewInvoicePanel();
				showSalesPanel(rightRenderer, centerRenderer);
				buildClientEditPanel();
				showClientsPanel(rightRenderer, centerRenderer);
				buildHelpGuide();
				buildAboutPane();
		ifSales.setVisible(false);
		lpa_menuBar.setVisible(false);
		ifLogin.setVisible(true);
		openDB();
	}
	
	public void showUsersPanel(DefaultTableCellRenderer rightRenderer,DefaultTableCellRenderer centerRenderer)
	{
		String[] columnNames = {
				"User ID",
                "Username",
                "First Name",
                "Last Name",
                "Group"
                };
		Object[][] dataUsers = null;
		ifSearchUsers = new JInternalFrame("LPA - Search Users");
		ifSearchUsers.setBounds(586, 198, 725, 312);
		contentPane.add(ifSearchUsers);
		ifSearchUsers.setFrameIcon(searchIcon);
		ifSearchUsers.setClosable(true);
		ifSearchUsers.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifSearchUsers.setBackground(new Color(35, 44, 49));
		ifSearchUsers.getContentPane().setLayout(null);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
		lblSearch.setForeground(new Color(102, 147, 182));
		lblSearch.setBounds(10, 14, 46, 14);
		ifSearchUsers.getContentPane().add(lblSearch);
		
		txtUsersSearch = new JTextField();
		txtUsersSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchUsersData(txtUsersSearch.getText().toString());
			}
		});
		txtUsersSearch.setForeground(Color.WHITE);
		txtUsersSearch.setBackground(Color.DARK_GRAY);
		txtUsersSearch.setBounds(66, 11, 534, 20);
		ifSearchUsers.getContentPane().add(txtUsersSearch);
		txtUsersSearch.setColumns(10);
		tblSearchUsers = new JTable();
		tblSearchUsers.setForeground(Color.WHITE);
		tblSearchUsers.setBackground(Color.DARK_GRAY);
		tblSearchUsers.setModel(new DefaultTableModel(dataUsers,columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       /* Set all cells to NON Editable 
		        *   - change return value to "true" for Editable 
		        * */ 
		       return false;
		    }
  }
		);
		tblSearchUsers.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(final MouseEvent e) {
		        if (e.getClickCount() == 1) {
		            final JTable target = (JTable)e.getSource();
		            final int row = target.getSelectedRow();
		            final int column = target.getSelectedColumn();
		            String val = target.getValueAt(row, 0).toString();
		            getUsersData(val);
		            saveMode="Update";
		            btnDeleteUser.setVisible(true);
		        }
		    }
		}); 
		tblSearchUsers.getColumnModel().getColumn(1).setPreferredWidth(50);
		tblSearchUsers.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblSearchUsers.getColumnModel().getColumn(3).setPreferredWidth(50);
		tblSearchUsers.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tblSearchUsers.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		
		searchScrollPaneUsers = new JScrollPane(tblSearchUsers);
		searchScrollPaneUsers.setBounds(10, 47, 689, 191);
		tblSearchUsers.setFillsViewportHeight(true);		
		ifSearchUsers.getContentPane().add(searchScrollPaneUsers);
		
		JButton btnClose = new JButton("Close");
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.DARK_GRAY);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSearchUsers.setVisible(false);
			}
		});
		btnClose.setBounds(610, 249, 89, 23);
		ifSearchUsers.getContentPane().add(btnClose);
		
				JButton btnNewUsers = new JButton("New");
				btnNewUsers.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Integer idUser = genID();
						txtUserID.setText(idUser.toString());
						txtEditUsername.setText("");
						txtUserFirstName.setText("");
			        	txtUserLastName.setText("");
			        	txtEditUserPassword.setText("");
						saveMode="new";
						btnDeleteUser.setVisible(false);
						centerJIF(ifUsers,"app"); 
						rdbtnActive.setSelected(true);
					}
				});
				btnNewUsers.setForeground(Color.WHITE);
				btnNewUsers.setBackground(Color.DARK_GRAY);
				btnNewUsers.setBounds(511, 249, 89, 23);
				
				JButton btnSearchUsers = new JButton("Search");
				btnSearchUsers.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchUsersData(txtUsersSearch.getText().toString());
					}
				});
				tblSearchUsers.getTableHeader().setForeground(Color.WHITE);
				tblSearchUsers.getTableHeader().setBackground(Color.DARK_GRAY);
				
				tblSearchUsers.getTableHeader().setReorderingAllowed(false);
				btnSearchUsers.setForeground(Color.WHITE);
				btnSearchUsers.setBackground(Color.DARK_GRAY);
				btnSearchUsers.setBounds(610, 11, 89, 23);
				ifSearchUsers.getContentPane().add(btnSearchUsers);
				
				ifSearchUsers.getContentPane().add(btnNewUsers);
				ifSearchUsers.setVisible(false);
	}
	
	public void centerJIF(JInternalFrame jif,String parent) {
		
		Dimension parentFrame=null,jInternalFrameSize=null;
		if(parent.equals("app")) {
			parentFrame = contentPane.getSize();
	    } else {
			parentFrame = screenDims;
	    }
	    jInternalFrameSize = jif.getSize();
		int width = (parentFrame.width - jInternalFrameSize.width) / 2;
	    int height = (parentFrame.height - jInternalFrameSize.height) / 2;
	    jif.setLocation(width, height);
	    jif.setVisible(true);
	}	
	public void MSG_POPUP(String MSG) {
		JOptionPane.showMessageDialog(contentPane,MSG);
	}
	public void openDB() {
        try {
			Class.forName("com.mysql.jdbc.Driver");
	        con = (Connection) DriverManager.getConnection(url, user, pass);
	        st = (Statement) con.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
	}
	
	public void do_login() {
		admin = false;
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
	        		"SELECT lpa_user_firstname,lpa_user_lastname,lpa_user_group,lpa_user_ID FROM lpa_users WHERE " + 
	        		"lpa_user_username = '" + txtUsername.getText() + "' AND " +
	        		"lpa_user_password = '" + new String(txtPassword.getPassword()) + 
	        		"' LIMIT 1;"
	        );
			if (rs.next()) {
				lpa_menuBar.setVisible(true);
				txtUsername.setText("");
				txtPassword.setText("");
				txtUsername.requestFocus();
				ifLogin.setVisible(false);
				lblDisplayName.setText(
						"Welcome " + rs.getString("lpa_user_firstname") 
							+ " " + rs.getString("lpa_user_lastname"));
				
				currentUserID = rs.getString("lpa_user_ID");
				
				if (rs.getString("lpa_user_group").equals(UserGroups.administrator.toString())) {

					admin = true;
					lpa_mnSystemAdmin.setVisible(true);
					lpa_mntmMyUserDetails.setVisible(false);
					btnNewClients.setVisible(true);
					btnNewStock.setVisible(true);
					btnNewSales.setVisible(true);
				}
				else
				{
					admin = false;
					lpa_mntmMyUserDetails.setVisible(true);
					lpa_mnSystemAdmin.setVisible(false);
					btnNewClients.setVisible(false);
					btnNewStock.setVisible(false);
					btnNewSales.setVisible(false);
				}
			} else {
	       	 MSG_POPUP("Login Failed!");
	        }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
	}

	public void do_logout() {
    	lpa_menuBar.setVisible(false);
    	txtUsername.setText("");
    	txtPassword.setText("");
		lblDisplayName.setText("");
		centerJIF(ifLogin,"app");
    	txtUsername.requestFocus();
		lpa_mnSystemAdmin.setVisible(false);
		lpa_mntmMyUserDetails.setVisible(false);
		admin = false;
		currentUserID = "";
	}
	
	public void searchStockData(final String SearhData) {
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
				"SELECT * FROM lpa_stock WHERE lpa_stock_status <> 'D' AND " + 
			    "(lpa_stock_ID LIKE '%" + SearhData + "%' OR " +
			    "lpa_stock_name LIKE '%" + SearhData + "%');" 
			);
			if(rs.next()) {
			 stockModel = (DefaultTableModel) tblSearchStock.getModel();
			 stockModel.getDataVector().removeAllElements();
			 stockModel.fireTableDataChanged();
           	 rs.beforeFirst();
           	 while(rs.next()) {
           		 Object[] row = {
	            			 rs.getString("lpa_stock_ID"),
	            			 rs.getString("lpa_stock_name"),
	            			 rs.getString("lpa_stock_onhand"), 
	            			 rs.getString("lpa_stock_price")
	 			     };
           		stockModel.addRow(row);
           	 }
		   }else {
          	 MSG_POPUP("No records found!");
           }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}		
	}

	public void getStockData(final String StockID) {
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
					"SELECT * FROM lpa_stock WHERE " + 
				    "lpa_stock_ID = '" + StockID + "' LIMIT 1;"
		    );
	        if(rs.next()) {
	        	txtStockID.setText(rs.getString("lpa_stock_ID"));
	        	txtStockName.setText(rs.getString("lpa_stock_name"));
	        	txtStockDes.setText(rs.getString("lpa_stock_desc"));
	        	txtStockOnHand.setText(rs.getString("lpa_stock_onhand"));
	        	txtStockPrice.setText(rs.getString("lpa_stock_price"));
	        	
	        	/*Dimension ifS = ifSearchStock.getSize();
	        	Point IFSS = ifSearchStock.getLocation();
	        	int ifsX = (int) IFSS.getX(); 
	        	int ifsY = (int) (IFSS.getY() + ifS.height) + 1; 
	        	ifStock.setLocation(ifsX,ifsY);*/
	        	ifStock.setVisible(true);
	        }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}		
	}
	//pass value by reference
	public void saveStockData(final String StockID) {
		try {
			if(validateStockInfo())
			{
				if(saveMode == "new") {
					st.executeUpdate("INSERT INTO lpa_stock "
							+ "(lpa_stock_ID,lpa_stock_name,lpa_stock_desc,lpa_stock_onhand,lpa_stock_price,lpa_stock_status) " + 
							"VALUES ('"
							+ txtStockID.getText() + "'"); 
					//complete the sql statement, matching database coln names and corresponding values
				} else {
					st.executeUpdate(
							"UPDATE lpa_stock SET " + 
						    "lpa_stock_ID = '" + txtStockID.getText() + "'," +  
						    "lpa_stock_name = '" + txtStockName.getText() + "'," +
						    "lpa_stock_desc = '" + txtStockDes.getText() + "'," +
						    "lpa_stock_onhand = '" + txtStockOnHand.getText() + "'," +
						    "lpa_stock_price = '" + txtStockPrice.getText() + "' " +
						    "WHERE lpa_stock_ID = '"+StockID+"' LIMIT 1;"
				    );
				}
				searchStockData(txtStockSearch.getText().toString());
				JOptionPane.showMessageDialog(null, "Record saved!");
	         	ifStock.setVisible(false);
			}
		} catch (SQLException ex) {
        	System.out.print(ex.getMessage().toString());
		}		
	}
	private void deleteStock(String text) {
		int dialogBtn = JOptionPane.YES_NO_OPTION;
		int result = JOptionPane.showConfirmDialog(null, "Are you sure of deleting this record?", "Warning", dialogBtn);

		if (result == JOptionPane.YES_OPTION) { 
			try {
				st.executeUpdate("UPDATE lpa_stock SET lpa_stock_status = 'D'" + " WHERE lpa_stock_ID = '"
						+ txtStockID.getText() + "' LIMIT 1;");
				searchStockData(txtStockSearch.getText().toString());
				JOptionPane.showMessageDialog(null, "Record deleted!");
				ifStock.setVisible(false);
			} catch (SQLException e) {
				System.out.print(e.getMessage().toString());
			}
		}
	}
	
	private void searchUsersData(final String searchData ) {
		try {

			ResultSet rs = (ResultSet) st.executeQuery(
				"SELECT * FROM lpa_users WHERE lpa_User_status <> 'D' AND " + 
			    "(lpa_User_ID LIKE '%" + searchData + "%' OR " +
			    "lpa_user_username LIKE '%" + searchData + "%');" 
			);
			if(rs.next()) {
			 usersModel = (DefaultTableModel) tblSearchUsers.getModel();
			 usersModel.getDataVector().removeAllElements();
			 usersModel.fireTableDataChanged();
           	 rs.beforeFirst();
           	 while(rs.next()) {
           		 Object[] row = {
	            			 rs.getString("lpa_user_ID"),
	            			 rs.getString("lpa_user_username"),
	            			 rs.getString("lpa_user_firstname"), 
	            			 rs.getString("lpa_user_lastname"),
	            			 rs.getString("lpa_user_group")
	 			     };
           		usersModel.addRow(row);
           	 }
		   }else {
          	 MSG_POPUP("No records found!");
           }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
		
	}
	private void getUsersData(String userID) {
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
					"SELECT * FROM lpa_users WHERE " + 
				    "lpa_user_ID = '" + userID + "' LIMIT 1;"
		    );
	        if(rs.next()) {
	        	txtUserID.setText(rs.getString("lpa_user_ID"));
	        	txtEditUsername.setText(rs.getString("lpa_user_username"));
	        	txtUserFirstName.setText(rs.getString("lpa_user_firstname"));
	        	txtUserLastName.setText(rs.getString("lpa_user_lastname"));
	        	cboxUserGroup.setSelectedItem(rs.getString("lpa_user_group"));
	        	txtEditUserPassword.setText(rs.getString("lpa_user_password"));
	        	if(rs.getString("lpa_user_status")=="D"){
	        		rdbtnInactive.setSelected(true);
	        		}
	        	else
	        	{
	        		rdbtnActive.setSelected(true);
	        	}
	        	/*
	        	Dimension ifS = ifSearchUsers.getSize();
	        	Point IFSS = ifSearchUsers.getLocation();
	        	int ifsX = (int) IFSS.getX(); 
	        	int ifsY = (int) (IFSS.getY() + ifS.height) + 1; 
	        	ifUsers.setLocation(ifsX,ifsY);*/
	        	ifUsers.setVisible(true);
	        }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
		
	}
	private void buildUserEditPanel()
	{
		ifUsers = new JInternalFrame("LPA - Users Record");
		ifUsers.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifUsers.setClosable(true);
		ifUsers.setBackground(new Color(35, 44, 49));
		ifUsers.setBounds(170, 30, 618, 323);
		contentPane.add(ifUsers);
		ifUsers.getContentPane().setLayout(null);
		//Label for UserID field
		JLabel lblUsersId = new JLabel("User ID:");
		lblUsersId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsersId.setForeground(Color.WHITE);
		lblUsersId.setBounds(10, 11, 80, 14);
		ifUsers.getContentPane().add(lblUsersId);
		// textbox for UserID
		txtUserID = new JTextField();
		txtUserID.setBackground(Color.DARK_GRAY);
		txtUserID.setForeground(Color.WHITE);
		txtUserID.setBounds(100, 9, 120, 20);
		txtUserID.setEditable(false);
		ifUsers.getContentPane().add(txtUserID);
		txtUserID.setColumns(10);
		//Label for Username textbox
		JLabel lblUserName = new JLabel("Username:");
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUserName.setBounds(10, 42, 120, 14);
		ifUsers.getContentPane().add(lblUserName);
		
		//Textbox for username
		txtEditUsername = new JTextField();
		txtEditUsername.setForeground(Color.WHITE);
		txtEditUsername.setColumns(10);
		txtEditUsername.setBackground(Color.DARK_GRAY);
		txtEditUsername.setBounds(100, 40, 120, 20);
		ifUsers.getContentPane().add(txtEditUsername);
		
		// User First Name textbox
		txtUserFirstName = new JTextField();
		txtUserFirstName.setForeground(Color.WHITE);
		txtUserFirstName.setColumns(10);
		txtUserFirstName.setBackground(Color.DARK_GRAY);
		txtUserFirstName.setBounds(100, 71, 120, 20);
		ifUsers.getContentPane().add(txtUserFirstName);
		
		//Label for the first name textbox
		JLabel lblUserFirstName = new JLabel("First Name:");
		lblUserFirstName.setForeground(Color.WHITE);
		lblUserFirstName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUserFirstName.setBounds(10, 73, 80, 14);
		ifUsers.getContentPane().add(lblUserFirstName);
		
		//textbox for User Last Name
		txtUserLastName = new JTextField();
		txtUserLastName.setForeground(Color.WHITE);
		txtUserLastName.setColumns(10);
		txtUserLastName.setBackground(Color.DARK_GRAY);
		txtUserLastName.setBounds(100, 102, 120, 20);
		ifUsers.getContentPane().add(txtUserLastName);
		
		//Label for User Last Name
		JLabel lblUserLastName = new JLabel("Last Name:");
		lblUserLastName.setForeground(Color.WHITE);
		lblUserLastName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUserLastName.setBounds(10, 104, 80, 14);
		ifUsers.getContentPane().add(lblUserLastName);
		
		//Group Combobox
		cboxUserGroup.setForeground(Color.WHITE);
		cboxUserGroup.setBackground(Color.DARK_GRAY);
		cboxUserGroup.setBounds(100, 133, 120, 20);
		ifUsers.getContentPane().add(cboxUserGroup);
		
		//Label for Password
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setForeground(Color.WHITE);
		lblGroup.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGroup.setBounds(10, 135, 80, 14);
		ifUsers.getContentPane().add(lblGroup);
		
		// textbox for User Password
		txtEditUserPassword = new JPasswordField();
		txtEditUserPassword.setForeground(Color.WHITE);
		txtEditUserPassword.setColumns(10);
		txtEditUserPassword.setBackground(Color.DARK_GRAY);
		txtEditUserPassword.setBounds(100, 164, 120, 20);
		ifUsers.getContentPane().add(txtEditUserPassword);

		// Label for Password
		JLabel lblUserPassword = new JLabel("Password:");
		lblUserPassword.setForeground(Color.WHITE);
		lblUserPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUserPassword.setBounds(10, 166, 80, 14);
		ifUsers.getContentPane().add(lblUserPassword);
		
		rdbtnActive = new JRadioButton("Active");
		rdbtnActive.setBounds(100, 201, 109, 23);
		ifUsers.getContentPane().add(rdbtnActive);
		
		rdbtnInactive = new JRadioButton("Inactive");
		rdbtnInactive.setBounds(218, 201, 109, 23);
		ifUsers.getContentPane().add(rdbtnInactive);
		
		ButtonGroup btnGroupStatus = new ButtonGroup();
		btnGroupStatus.add(rdbtnActive);
		btnGroupStatus.add(rdbtnInactive);
		
		rdbtnActive.setSelected(true);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStatus.setBounds(10, 199, 80, 14);
		
		JButton btnUserSave = new JButton("Save");
		btnUserSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validateUserInfo())
				{
					saveUserData(txtUserID.getText());
				}
			}
		});
		btnUserSave.setForeground(Color.WHITE);
		btnUserSave.setBackground(Color.DARK_GRAY);
		btnUserSave.setBounds(503, 244, 89, 23);
		ifUsers.getContentPane().add(btnUserSave);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 231, 582, 2);
		ifUsers.getContentPane().add(separator_1);
		
		btnDeleteUser = new JButton("Delete");
		btnDeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteUser(txtUserID.getText());
			}
		});
		btnDeleteUser.setBounds(388, 244, 89, 23);
		ifUsers.getContentPane().add(btnDeleteUser);
		
		ifUsers.getContentPane().add(lblStatus);
		ifUsers.setVisible(false); 
	}
	
	public void showSalesPanel(DefaultTableCellRenderer rightRenderer,DefaultTableCellRenderer centerRenderer)
	{
		String[] columnNames = {
				"Invoice Number",
                "Client Name",
                "Date",
                "Amount",
                };
		Object[][] dataSales = null;
		ifSales = new JInternalFrame("LPA - Search Sales");
		ifSales.setBounds(586, 198, 725, 312);
		contentPane.add(ifSales);
		ifSales.setFrameIcon(searchIcon);
		ifSales.setClosable(true);
		ifSales.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifSales.setBackground(new Color(35, 44, 49));
		ifSales.getContentPane().setLayout(null);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
		lblSearch.setForeground(new Color(102, 147, 182));
		lblSearch.setBounds(10, 14, 46, 14);
		ifSales.getContentPane().add(lblSearch);
		
		txtSalesSearch = new JTextField();
		txtSalesSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchInvoicesData(txtSalesSearch.getText().toString());
			}
		});
		txtSalesSearch.setForeground(Color.WHITE);
		txtSalesSearch.setBackground(Color.DARK_GRAY);
	
		txtSalesSearch.setBounds(66, 11, 534, 20);
		ifSales.getContentPane().add(txtSalesSearch);
		txtSalesSearch.setColumns(10);
		tblSearchSales = new JTable();
		tblSearchSales.setForeground(Color.WHITE);
		tblSearchSales.setBackground(Color.DARK_GRAY);
		tblSearchSales.setModel(new DefaultTableModel(dataSales,columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       /* Set all cells to NON Editable 
		        *   - change return value to "true" for Editable 
		        * */ 
		       return false;
		    }
  }
		);

		tblSearchSales.getColumnModel().getColumn(1).setPreferredWidth(50);
		tblSearchSales.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblSearchSales.getColumnModel().getColumn(3).setPreferredWidth(50);
		tblSearchSales.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tblSearchSales.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		
		searchScrollPaneSales = new JScrollPane(tblSearchSales);
		searchScrollPaneSales.setBounds(10, 47, 689, 191);
		tblSearchSales.setFillsViewportHeight(true);		
		ifSales.getContentPane().add(searchScrollPaneSales);
		
		JButton btnClose = new JButton("Close");
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.DARK_GRAY);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSales.setVisible(false);
			}
		});
		btnClose.setBounds(610, 249, 89, 23);
		ifSales.getContentPane().add(btnClose);
		
				btnNewSales = new JButton("New");
				btnNewSales.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						txtSalesSearch.setText("");
						saveMode="new";
						centerJIF(ifNewInvoice,"app"); 
						totalAmountInvoice = 0F;
					}
				});
				btnNewSales.setForeground(Color.WHITE);
				btnNewSales.setBackground(Color.DARK_GRAY);
				btnNewSales.setBounds(511, 249, 89, 23);
				
				JButton btnSearchSales = new JButton("Search");
				btnSearchSales.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchInvoicesData(txtSalesSearch.getText().toString());
					}
				});
				tblSearchSales.getTableHeader().setForeground(Color.WHITE);
				tblSearchSales.getTableHeader().setBackground(Color.DARK_GRAY);
				
				tblSearchSales.getTableHeader().setReorderingAllowed(false);
				btnSearchSales.setForeground(Color.WHITE);
				btnSearchSales.setBackground(Color.DARK_GRAY);
				btnSearchSales.setBounds(610, 11, 89, 23);
				ifSales.getContentPane().add(btnSearchSales);
				
				ifSales.getContentPane().add(btnNewSales);
				ifSales.setVisible(false);
	}
	
	private void saveUserData(String userID) {
		String status = (rdbtnActive.isSelected())?"a":"D";
		try {
			if(saveMode == "new") {
				st.executeUpdate("INSERT INTO lpa_users "
						+ "(lpa_user_ID,lpa_user_username,lpa_user_firstname,lpa_user_lastname,lpa_user_group,lpa_user_password,lpa_user_status) " + "VALUES ('"
						+ txtUserID.getText() + "','" + txtEditUsername.getText() + "','" + txtUserFirstName.getText() + "','"
						+ txtUserLastName.getText() + "','" + cboxUserGroup.getSelectedItem() + "','"+new String(txtEditUserPassword.getPassword())+"','"+ status+"');");
			} else {
				st.executeUpdate(
						"UPDATE lpa_users SET " + 
					    "lpa_user_ID = '" + txtUserID.getText() + "'," +  
					    "lpa_user_username = '" + txtEditUsername.getText() + "'," +
					    "lpa_user_lastname = '" + txtUserLastName.getText() + "'," +
					    "lpa_user_firstname = '" + txtUserFirstName.getText() + "'," +
					    "lpa_user_group = '" + cboxUserGroup.getSelectedItem() + "', " +
					    "lpa_user_password = '"+new String(txtEditUserPassword.getPassword())+"', "+
					    "lpa_user_status = '" + status + "' "+
					    "WHERE lpa_user_ID = '"+userID+"' LIMIT 1;"
			    );
			}
			searchUsersData(txtUsersSearch.getText().toString());
			JOptionPane.showMessageDialog(null, "Record saved!");
         	ifUsers.setVisible(false);
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
		
	}
	
	private void deleteUser(String text) {
		int dialogBtn = JOptionPane.YES_NO_OPTION;
		int result = JOptionPane.showConfirmDialog(null, "Are you sure of deleting this record?", "Warning", dialogBtn);

		if (result == JOptionPane.YES_OPTION) { 
			try {
				st.executeUpdate("UPDATE lpa_users SET lpa_user_status = 'D'" + " WHERE lpa_user_ID = '"
						+ txtUserID.getText() + "' LIMIT 1;");
				searchUsersData(txtUsersSearch.getText().toString());
				JOptionPane.showMessageDialog(null, "Record deleted!");
				ifUsers.setVisible(false);
			} catch (SQLException e) {
				System.out.print(e.getMessage().toString());
			}
		}
		
	}
	

	private void searchInvoicesData(String searchData ) {
		try {

			ResultSet rs = (ResultSet) st.executeQuery(
				"SELECT * FROM lpa_invoices WHERE lpa_inv_status <> 'D' AND " + 
			    "(lpa_inv_no LIKE '%" + searchData + "%' OR " +
			    "lpa_inv_client_name LIKE '%" + searchData + "%' OR "+
				"lpa_inv_date LIKE '%" + searchData + "%');" 
			);
			if(rs.next()) {
			 invoicesModel = (DefaultTableModel) tblSearchSales.getModel();
			invoicesModel.getDataVector().removeAllElements();
			 invoicesModel.fireTableDataChanged();
           	 rs.beforeFirst();
			 float total = 0F;
           	 while(rs.next()) {
           		 Object[] row = {
	            			 rs.getString("lpa_inv_no"),
	            			 rs.getString("lpa_inv_client_name"),
	            			 rs.getString("lpa_inv_date"), 
	            			 rs.getString("lpa_inv_amount"),
	 			     };
           		invoicesModel.addRow(row);
				total+=rs.getFloat("lpa_inv_amount");
           	 }
			 Object [] rowTotal ={"Total","","",total};
			 invoicesModel.addRow(rowTotal);
		   }else {
          	 MSG_POPUP("No records found!");
           }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
		
	}
	
	private void buildNewInvoicePanel()
	{
		
		ifNewInvoice = new JInternalFrame("LPA - New Invoice");
		ifNewInvoice.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifNewInvoice.setClosable(true);
		ifNewInvoice.setBackground(new Color(35, 44, 49));
		ifNewInvoice.setBounds(86, 92, 717, 460);
		contentPane.add(ifNewInvoice);
		ifNewInvoice.getContentPane().setLayout(null);
		JLabel lblClienteIdSearch = new JLabel("Client ID");
		lblClienteIdSearch.setFont(new Font("Arial", Font.BOLD, 12));
		lblClienteIdSearch.setForeground(Color.WHITE);
		lblClienteIdSearch.setBounds(35, 34, 59, 14);
		ifNewInvoice.add(lblClienteIdSearch);
		
		txtClientIDSearch = new JTextField();
		txtClientIDSearch.setBounds(99, 31, 86, 20);
		txtClientIDSearch.setForeground(Color.WHITE);
		txtClientIDSearch.setBackground(Color.DARK_GRAY);
		ifNewInvoice.add(txtClientIDSearch);
		txtClientIDSearch.setColumns(10);
		
		JButton btnSearchClient = new JButton("Search Client");
		btnSearchClient.setBounds(195, 30, 112, 23);
		btnSearchClient.setForeground(Color.WHITE);
		btnSearchClient.setBackground(Color.DARK_GRAY);
		btnSearchClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchClient(txtClientIDSearch.getText().toString());
			}
		});
		ifNewInvoice.add(btnSearchClient);
		
		JLabel lblClienteId = new JLabel("Client ID");
		lblClienteId.setFont(new Font("Arial", Font.BOLD, 12));
		lblClienteId.setForeground(Color.WHITE);
		lblClienteId.setBounds(35, 79, 59, 14);
		ifNewInvoice.add(lblClienteId);
		
		txtCliendId = new JTextField();
		txtCliendId.setEditable(false);
		txtCliendId.setBounds(99, 76, 86, 20);
		txtCliendId.setForeground(Color.WHITE);
		txtCliendId.setBackground(Color.DARK_GRAY);
		ifNewInvoice.add(txtCliendId);
		txtCliendId.setColumns(10);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setBounds(233, 76, 131, 20);
		txtName.setForeground(Color.WHITE);
		txtName.setBackground(Color.DARK_GRAY);
		ifNewInvoice.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("Arial", Font.BOLD, 12));
		lblName.setBounds(195, 79, 46, 14);
		ifNewInvoice.add(lblName);
		
		JLabel lblAdress = new JLabel("Adress");
		lblAdress.setBounds(372, 79, 46, 14);
		lblAdress.setFont(new Font("Arial", Font.BOLD, 12));
		lblAdress.setForeground(Color.WHITE);
		ifNewInvoice.add(lblAdress);
		
		txtAddress = new JTextField();
		txtAddress.setEditable(false);
		txtAddress.setForeground(Color.WHITE);
		txtAddress.setBackground(Color.DARK_GRAY);
		txtAddress.setBounds(424, 76, 86, 20);
		ifNewInvoice.add(txtAddress);
		txtAddress.setColumns(10);
		
		txtPhone = new JTextField();
		txtPhone.setEditable(false);
		txtPhone.setForeground(Color.WHITE);
		txtPhone.setBackground(Color.DARK_GRAY);
		txtPhone.setBounds(560, 76, 86, 20);
		ifNewInvoice.add(txtPhone);
		txtPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(518, 79, 46, 14);
		lblPhone.setFont(new Font("Arial", Font.BOLD, 12));
		lblPhone.setForeground(Color.WHITE);
		ifNewInvoice.add(lblPhone);
		
		JLabel lblStockId = new JLabel("Stock ID");
		lblStockId.setBounds(35, 129, 59, 14);
		lblStockId.setFont(new Font("Arial", Font.BOLD, 12));
		lblStockId.setForeground(Color.WHITE);
		ifNewInvoice.add(lblStockId);
		
		txtInvStockID = new JTextField();
		txtInvStockID.setBounds(99, 126, 86, 20);
		txtInvStockID.setForeground(Color.WHITE);
		txtInvStockID.setBackground(Color.DARK_GRAY);
		ifNewInvoice.add(txtInvStockID);
		txtInvStockID.setColumns(10);
		
		JButton btnSearchItem = new JButton("Search Item");
		btnSearchItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchItem(txtInvStockID.getText().toString());
			}
		});
		btnSearchItem.setForeground(Color.WHITE);
		btnSearchItem.setBackground(Color.DARK_GRAY);
		btnSearchItem.setBounds(195, 125, 108, 23);
		ifNewInvoice.add(btnSearchItem);
		
		JLabel lblStockId_1 = new JLabel("Item ID");
		lblStockId_1.setFont(new Font("Arial", Font.BOLD, 12));
		lblStockId_1.setForeground(Color.WHITE);
		lblStockId_1.setBounds(35, 171, 46, 14);
		ifNewInvoice.add(lblStockId_1);
		
		txtItemID = new JTextField();
		txtItemID.setBounds(99, 168, 86, 20);
		txtItemID.setForeground(Color.WHITE);
		txtItemID.setBackground(Color.DARK_GRAY);
		txtItemID.setEditable(false);
		ifNewInvoice.add(txtItemID);
		txtItemID.setColumns(10);
		
		JLabel lblNameItem = new JLabel("Name");
		lblNameItem.setFont(new Font("Arial", Font.BOLD, 12));
		lblNameItem.setForeground(Color.WHITE);
		lblNameItem.setBounds(195, 171, 46, 14);
		ifNewInvoice.add(lblNameItem);
		
		txtNameItem = new JTextField();
		txtNameItem.setBounds(233, 168, 115, 20);
		txtNameItem.setForeground(Color.WHITE);
		txtNameItem.setBackground(Color.DARK_GRAY);
		txtNameItem.setEditable(false);
		ifNewInvoice.add(txtNameItem);
		txtNameItem.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(358, 171, 46, 14);
		lblPrice.setFont(new Font("Arial", Font.BOLD, 12));
		lblPrice.setForeground(Color.WHITE);
		ifNewInvoice.add(lblPrice);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(392, 168, 70, 20);
		txtPrice.setForeground(Color.WHITE);
		txtPrice.setBackground(Color.DARK_GRAY);
		txtPrice.setEditable(false);
		ifNewInvoice.add(txtPrice);
		txtPrice.setColumns(10);
		
		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setBounds(473, 171, 50, 14);
		lblQuantity.setFont(new Font("Arial", Font.BOLD, 12));
		lblQuantity.setForeground(Color.WHITE);
		ifNewInvoice.add(lblQuantity);
		
		txtQuantity = new JTextField();
		txtQuantity.setBounds(525, 168, 36, 20);
		txtQuantity.setForeground(Color.WHITE);
		txtQuantity.setBackground(Color.DARK_GRAY);		
		ifNewInvoice.add(txtQuantity);
		txtQuantity.setColumns(10);
		
		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.setBounds(575, 167, 95, 23);
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtItemID.getText()!="") {
					addItem();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Search for the Stock Item to add!");
				}
			}
		});
		btnAddItem.setForeground(Color.WHITE);
		btnAddItem.setBackground(Color.DARK_GRAY);	
		ifNewInvoice.add(btnAddItem);
		
		String[] columnNames = {
				"Item ID",
                "Name",
                "Price",
                "Quantity",
                "Value"
                };
		Object[][] invoicedItems = null;
		tblInvoicedItems = new JTable(new DefaultTableModel(invoicedItems,columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       /* Set all cells to NON Editable 
		        *   - change return value to "true" for Editable 
		        **/ 
		       return false;
		    }
  }
		);
		tblInvoicedItems.setForeground(Color.WHITE);
		tblInvoicedItems.setBackground(Color.BLACK);
		
		newInvoiceScrollPane = new JScrollPane(tblInvoicedItems);
		
		newInvoiceScrollPane.setBounds(62, 212, 544, 159);
		
		tblInvoicedItems.setFillsViewportHeight(true);
		ifNewInvoice.add(newInvoiceScrollPane);
		invoicedTtems = (DefaultTableModel) tblInvoicedItems.getModel();
		JButton btnSaveInvoice = new JButton("Save Invoice");
		btnSaveInvoice.setBounds(403, 383, 105, 23);
		btnSaveInvoice.setForeground(Color.WHITE);
		btnSaveInvoice.setBackground(Color.DARK_GRAY);
		btnSaveInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validateInvoice())
				{
					saveInvoice();
				}
			}
		});
		ifNewInvoice.add(btnSaveInvoice);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(512, 383, 95, 23);
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.DARK_GRAY);
		ifNewInvoice.add(btnClose);
	}
	
	private void searchItem(String SearchData) {
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
				"SELECT * FROM lpa_stock WHERE lpa_stock_ID  = " + SearchData + ";" 
			);
			if(rs.next()) {

	            			 txtItemID.setText(rs.getString("lpa_stock_ID"));
	            			 txtNameItem.setText(rs.getString("lpa_stock_name"));
	            			 txtPrice.setText(rs.getString("lpa_stock_price")) ;
	            			 txtInvStockID.setText("");
           	 }else {
          	 MSG_POPUP("No records found!");
           }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
		
	}
	
	private void searchClient(String SearchData) {
		try {
			ResultSet rs = (ResultSet) st
					.executeQuery("SELECT * FROM lpa_clients WHERE lpa_client_ID  = " + SearchData + ";");
			if (rs.next()) {

				txtCliendId.setText(rs.getString("lpa_client_ID"));
				txtName.setText(rs.getString("lpa_client_firstname") + " " + rs.getString("lpa_client_lastname"));
				txtAddress.setText(rs.getString("lpa_client_address"));
				txtPhone.setText(rs.getString("lpa_client_phone"));
				txtClientIDSearch.setText("");
			} else {
				MSG_POPUP("No records found!");
			}
		} catch (SQLException e) {
			System.out.print(e.getMessage().toString());
		}
		
	}
	
	private void addItem() {
		Float amountItem = new Float(txtPrice.getText()) * new Float(txtQuantity.getText());
		Object[] row = { txtItemID.getText(), txtNameItem.getText(), txtPrice.getText(), txtQuantity.getText(),
				 amountItem};
		invoicedTtems.addRow(row);
		txtItemID.setText("");
		txtNameItem.setText("");
		txtPrice.setText("");
		txtQuantity.setText("");
		totalAmountInvoice+=amountItem;
	}
	
	public void saveInvoice() {
		try {
			int invoiceNumber = genID();
			
			SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			st.executeUpdate(
					"INSERT INTO lpa_invoices (lpa_inv_no,lpa_inv_date,lpa_inv_client_ID,lpa_inv_client_name,lpa_inv_amount,lpa_inv_client_address,lpa_inv_status) "
							+ "VALUES (" + invoiceNumber + ",'" + sDF.format(new Date()) + "','" + txtCliendId.getText()
							+ "','" + txtName.getText() + "'," + totalAmountInvoice + ",'"+txtAddress.getText() +"','A');");
			
			for(int row = 0;row < invoicedTtems.getRowCount();row++) {
				
				st.executeUpdate(
						"INSERT INTO lpa_invoice_items (lpa_invitem_inv_no,lpa_invitem_stock_ID,"
						+"lpa_invitem_stock_name,lpa_invitem_qty,lpa_invitem_stock_price,lpa_invitem_stock_amount,lpa_inv_status) "
								+"VALUES (" +invoiceNumber+","+invoicedTtems.getValueAt(row, 0)+",'"+invoicedTtems.getValueAt(row, 1)+"',"+
								invoicedTtems.getValueAt(row, 3)+","+invoicedTtems.getValueAt(row, 2)+","+invoicedTtems.getValueAt(row, 4)
								+",'a');");
				}

			searchInvoicesData(txtSalesSearch.getText().toString());
			JOptionPane.showMessageDialog(null, "Record saved!");
			ifNewInvoice.setVisible(false);
			totalAmountInvoice=0F;
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}		
	}

	public void showClientsPanel(DefaultTableCellRenderer rightRenderer,DefaultTableCellRenderer centerRenderer)
	{
		String[] columnNames = {
				"Client ID",
                "First Name",
                "Last Name",
                "Address",
                "Phone"
                };
		Object[][] dataClients = null;
		ifSearchClients = new JInternalFrame("LPA - Search Clients");
		ifSearchClients.setBounds(586, 198, 725, 312);
		contentPane.add(ifSearchClients);
		ifSearchClients.setFrameIcon(searchIcon);
		ifSearchClients.setClosable(true);
		ifSearchClients.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifSearchClients.setBackground(new Color(35, 44, 49));
		ifSearchClients.getContentPane().setLayout(null);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Arial", Font.BOLD, 12));
		lblSearch.setForeground(new Color(102, 147, 182));
		lblSearch.setBounds(10, 14, 46, 14);
		ifSearchClients.getContentPane().add(lblSearch);
		
		txtClientsSearch = new JTextField();
		txtClientsSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchClientsData(txtClientsSearch.getText().toString());
			}
		});
		txtClientsSearch.setForeground(Color.WHITE);
		txtClientsSearch.setBackground(Color.DARK_GRAY);
		txtClientsSearch.setBounds(66, 11, 534, 20);
		ifSearchClients.getContentPane().add(txtClientsSearch);
		txtClientsSearch.setColumns(10);
		tblSearchClients = new JTable();
		tblSearchClients.setForeground(Color.WHITE);
		tblSearchClients.setBackground(Color.DARK_GRAY);
		tblSearchClients.setModel(new DefaultTableModel(dataClients,columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       /* Set all cells to NON Editable 
		        *   - change return value to "true" for Editable 
		        * */ 
		       return false;
		    }
  }
		);
		tblSearchClients.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(final MouseEvent e) {
		    	if(admin)
		    	{
			        if (e.getClickCount() == 1) {
			            final JTable target = (JTable)e.getSource();
			            final int row = target.getSelectedRow();
			            final int column = target.getSelectedColumn();
			            String val = target.getValueAt(row, 0).toString();
			            getClientsData(val);
			            saveMode="Update";
			            btnDeleteClient.setVisible(true);
			        }
		    	}
		    }
		}); 
		tblSearchClients.getColumnModel().getColumn(1).setPreferredWidth(50);
		tblSearchClients.getColumnModel().getColumn(2).setPreferredWidth(30);
		tblSearchClients.getColumnModel().getColumn(3).setPreferredWidth(50);
		tblSearchClients.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tblSearchClients.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		
		searchScrollPaneClients = new JScrollPane(tblSearchClients);
		searchScrollPaneClients.setBounds(10, 47, 689, 191);
		tblSearchClients.setFillsViewportHeight(true);		
		ifSearchClients.getContentPane().add(searchScrollPaneClients);
		
		JButton btnClose = new JButton("Close");
		btnClose.setForeground(Color.WHITE);
		btnClose.setBackground(Color.DARK_GRAY);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifSearchClients.setVisible(false);
			}
		});
		btnClose.setBounds(610, 249, 89, 23);
		ifSearchClients.getContentPane().add(btnClose);
		
				btnNewClients = new JButton("New");
				btnNewClients.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						txtEditClientID.setText(new Integer(genID()).toString());
						txtClientFirstName.setText("");
			        	txtClientLastName.setText("");
			        	txtClientAddress.setText("");
			        	txtClientPhone.setText("");
						saveMode="new";
						btnDeleteClient.setVisible(false);
						centerJIF(ifEditClient,"app"); 
					}
				});
				btnNewClients.setForeground(Color.WHITE);
				btnNewClients.setBackground(Color.DARK_GRAY);
				btnNewClients.setBounds(511, 249, 89, 23);
				
				JButton btnSearchClients = new JButton("Search");
				btnSearchClients.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						searchClientsData(txtClientsSearch.getText().toString());
					}
				});
				tblSearchClients.getTableHeader().setForeground(Color.WHITE);
				tblSearchClients.getTableHeader().setBackground(Color.DARK_GRAY);
				
				tblSearchClients.getTableHeader().setReorderingAllowed(false);
				btnSearchClients.setForeground(Color.WHITE);
				btnSearchClients.setBackground(Color.DARK_GRAY);
				btnSearchClients.setBounds(610, 11, 89, 23);
				ifSearchClients.getContentPane().add(btnSearchClients);
				
				ifSearchClients.getContentPane().add(btnNewClients);
				ifSearchClients.setVisible(false);
	}
	
	private void searchClientsData(String searchData) {
		try {

			ResultSet rs = (ResultSet) st.executeQuery(
				"SELECT * FROM lpa_clients WHERE lpa_client_status  'D' AND " + //insert appropriate condition
			    "(lpa_client_ID LIKE '%" + searchData + "%' OR " +
			    "lpa_client_firstname LIKE '%" + searchData + "%' OR lpa_client_lastname LIKE '%"+ searchData + "%');" 
			);
			if(rs.next()) {
			 clientsModel = (DefaultTableModel) tblSearchClients.getModel();
			 clientsModel.getDataVector().removeAllElements();
			 clientsModel.fireTableDataChanged();
           	 rs.beforeFirst();
           	 while(rs.next()) {
           		 Object[] row = {
	            			 rs.getString("lpa_client_ID"),
	            			 rs.getString("lpa_client_firstname"), 
	            			 rs.getString("lpa_client_lastname"),
							 rs.getString("lpa_client_address"),
	            			 rs.getString("lpa_client_phone")
	 			     };
           		clientsModel.addRow(row);
           	 }
		   }else {
          	 MSG_POPUP("No records found!");
           }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}
	}
	

	private void getClientsData(String clientID) {
		try {
			ResultSet rs = (ResultSet) st.executeQuery(
					"SELECT * FROM lpa_clients WHERE " + 
				    "lpa_client_ID = '" + clientID + "' LIMIT 1;"
		    );
	        if(rs.next()) {
	        	txtEditClientID.setText(rs.getString("lpa_client_ID"));
	        	txtClientFirstName.setText(rs.getString("lpa_client_firstname"));
	        	txtClientLastName.setText(rs.getString("lpa_client_lastname"));
	        	txtClientAddress.setText(rs.getString("lpa_client_address"));
	        	txtClientPhone.setText(rs.getString("lpa_client_phone"));
	        	
	        	/*Dimension ifS = ifSearchclient.getSize();
	        	Point IFSS = ifSearchclient.getLocation();
	        	int ifsX = (int) IFSS.getX(); 
	        	int ifsY = (int) (IFSS.getY() + ifS.height) + 1; 
	        	ifclient.setLocation(ifsX,ifsY);*/
	        	ifEditClient.setVisible(true);
	        }
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
		}		

	}
	
	private void buildClientEditPanel()
	{
		ifEditClient = new JInternalFrame("LPA - Clients Record");
		ifEditClient.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		ifEditClient.setClosable(true);
		ifEditClient.setBackground(new Color(35, 44, 49));
		ifEditClient.setBounds(170, 30, 618, 323);
		contentPane.add(ifEditClient);
		ifEditClient.getContentPane().setLayout(null);
		
		
		JLabel lblClientId = new JLabel("Client ID:");
		lblClientId.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientId.setForeground(Color.WHITE);
		lblClientId.setBounds(60, 51, 80, 14);
		ifEditClient.getContentPane().add(lblClientId);
		// textbox for ClientID
		txtEditClientID = new JTextField();
		txtEditClientID.setBackground(Color.DARK_GRAY);
		txtEditClientID.setForeground(Color.WHITE);
		txtEditClientID.setBounds(150, 49, 120, 20);
		txtEditClientID.setEditable(false);
		ifEditClient.getContentPane().add(txtEditClientID);
		txtEditClientID.setColumns(10);
		//Label for First Name textbox
		JLabel lblClientFirstName = new JLabel("First Name:");
		lblClientFirstName.setForeground(Color.WHITE);
		lblClientFirstName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientFirstName.setBounds(60, 82, 120, 14);
		ifEditClient.getContentPane().add(lblClientFirstName);
		
		//Textbox for First Name
		txtClientFirstName = new JTextField();
		txtClientFirstName.setForeground(Color.WHITE);
		txtClientFirstName.setColumns(10);
		txtClientFirstName.setBackground(Color.DARK_GRAY);
		txtClientFirstName.setBounds(150, 80, 120, 20);
		ifEditClient.getContentPane().add(txtClientFirstName);
		
		//Label for the Last name textbox
		JLabel lblClientLastName = new JLabel("Last Name:");
		lblClientLastName.setForeground(Color.WHITE);
		lblClientLastName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientLastName.setBounds(60, 113, 80, 14);
		ifEditClient.getContentPane().add(lblClientLastName);
		
		// Client Last Name textbox
		txtClientLastName = new JTextField();
		txtClientLastName.setForeground(Color.WHITE);
		txtClientLastName.setColumns(10);
		txtClientLastName.setBackground(Color.DARK_GRAY);
		txtClientLastName.setBounds(150, 107, 120, 20);
		ifEditClient.getContentPane().add(txtClientLastName);
		
		//Label for Client Address
		JLabel lblClientAddress = new JLabel("Address:");
		lblClientAddress.setForeground(Color.WHITE);
		lblClientAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientAddress.setBounds(60, 138, 80, 14);
		ifEditClient.getContentPane().add(lblClientAddress);
		
		// Client Address textbox
		txtClientAddress = new JTextField();
		txtClientAddress.setForeground(Color.WHITE);
		txtClientAddress.setColumns(10);
		txtClientAddress.setBackground(Color.DARK_GRAY);
		txtClientAddress.setBounds(150, 138, 120, 20);
		ifEditClient.getContentPane().add(txtClientAddress);
		
		//Label for Client Phone
		JLabel lblClientPhone = new JLabel("Phone:");
		lblClientPhone.setForeground(Color.WHITE);
		lblClientPhone.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblClientPhone.setBounds(60, 171, 80, 14);
		ifEditClient.getContentPane().add(lblClientPhone);
		
		// Client Phone textbox
		txtClientPhone = new JTextField();
		txtClientPhone.setForeground(Color.WHITE);
		txtClientPhone.setColumns(10);
		txtClientPhone.setBackground(Color.DARK_GRAY);
		txtClientPhone.setBounds(150, 169, 120, 20);
		ifEditClient.getContentPane().add(txtClientPhone);
		
		JButton btnClientSave = new JButton("Save");

		btnClientSave.setForeground(Color.WHITE);
		btnClientSave.setBackground(Color.DARK_GRAY);
		btnClientSave.setBounds(211, 238, 89, 23);
		btnClientSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validateClientInfo())
				{
					saveClientData(txtEditClientID.getText());
				}
			}
		});
		ifEditClient.getContentPane().add(btnClientSave);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 213, 307, 2);
		ifEditClient.getContentPane().add(separator_1);
		
		btnDeleteClient = new JButton("Delete");
		btnDeleteClient.setForeground(Color.WHITE);
		btnDeleteClient.setBackground(Color.DARK_GRAY);
		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteClient(txtEditClientID.getText());
			}
		});
		btnDeleteClient.setBounds(96, 238, 89, 23);
		ifEditClient.getContentPane().add(btnDeleteClient);
		ifEditClient.setVisible(false);
	}

	private void deleteClient(String clientID) {
		int dialogBtn = JOptionPane.YES_NO_OPTION;
		int result = JOptionPane.showConfirmDialog(null, "Are you sure of deleting this record?", "Warning", dialogBtn);

		if (result == JOptionPane.YES_OPTION) { 
			try {
				st.executeUpdate("UPDATE lpa_clients SET lpa_client_status = 'D'" + " WHERE lpa_client_ID = '"
						+ clientID + "' LIMIT 1;");
				searchClientsData(txtClientsSearch.getText().toString());
				JOptionPane.showMessageDialog(null, "Record deleted!");
				ifEditClient.setVisible(false);
			} catch (SQLException e) {
				System.out.print(e.getMessage().toString());
			}
		}
		
	}
	
	private void saveClientData(String clientID) {
		try {
			if(saveMode == "new") {
				st.executeUpdate("INSERT INTO lpa_clients "
						+ "(lpa_client_ID,lpa_client_firstname,lpa_client_lastname,lpa_client_address,lpa_client_phone,lpa_client_status) " + "VALUES ('"
						+ txtEditClientID.getText() + "','" + txtClientFirstName.getText() + "','"+ txtClientLastName.getText() 
						+ "','" + txtClientAddress.getText() + "','"+txtClientPhone.getText()+"','a');");
			} else {
				st.executeUpdate(
						"UPDATE lpa_clients SET " + 
					    "lpa_client_ID = '" + txtEditClientID.getText() + "'," +  
					    "lpa_client_lastname = '" + txtClientLastName.getText() + "'," +
					    "lpa_client_firstname = '" + txtClientFirstName.getText() + "'," +
					    "lpa_client_address = '" + txtClientAddress.getText() + "', " +
					    "lpa_client_phone = '"+txtClientPhone.getText()+"'"+
					    "WHERE lpa_client_ID = '"+clientID+"' LIMIT 1;"
			    );
			}
			searchClientsData(txtClientsSearch.getText().toString());
			JOptionPane.showMessageDialog(null, "Record saved!");
         	ifEditClient.setVisible(false);
		} catch (SQLException e) {
        	System.out.print(e.getMessage().toString());
}
		
	}
	
	private void buildHelpGuide() 
	{
		try {
			ifHelpGuide = new JInternalFrame("Help Guide");
			
			ifHelpGuide.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			ifHelpGuide.setClosable(true);
			ifHelpGuide.setBackground(new Color(35, 44, 49));
			ifHelpGuide.setBounds(86, 92, 1080, 460);
			contentPane.add(ifHelpGuide);
			// Getting the url with the html file path
			URL index = ClassLoader.getSystemResource("index.html");
			JEditorPane editorpane = new JEditorPane();
			editorpane.setEditable(false);
			editorpane.setPage(index);
			ifHelpGuide.getContentPane().add(new JScrollPane(editorpane));
			JPanel panebuttons = new JPanel();
			JButton btnclose = new JButton("Close");
			btnclose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ifHelpGuide.setVisible(false);
				}
			});
			panebuttons.add(btnclose);
			// add panel south
			ifHelpGuide.getContentPane().add(panebuttons, BorderLayout.SOUTH);
		} catch (IOException e) {
			System.out.print(e.getMessage().toString());
		}
	}
	
	
	private void buildAboutPane() 
	{
		try {
			ifAbout = new JInternalFrame("Help Guide");
			
			ifAbout.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			ifAbout.setClosable(true);
			ifAbout.setBackground(new Color(35, 44, 49));
			ifAbout.setBounds(86, 92, 430, 375);
			contentPane.add(ifAbout);
			// Getting the url with the html file path
			URL index = ClassLoader.getSystemResource("about.html");
			JEditorPane editorpane = new JEditorPane();
			editorpane.setEditable(false);
			editorpane.setPage(index);
			ifAbout.getContentPane().add(editorpane);
			JPanel panebuttons = new JPanel();
			JButton btnclose = new JButton("Close");
			btnclose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ifAbout.setVisible(false);
				}
			});
			panebuttons.add(btnclose);
			// add panel south
			ifAbout.getContentPane().add(panebuttons, BorderLayout.SOUTH);
		} catch (IOException e) {
			System.out.print(e.getMessage().toString());
		}
	}
	
	private int genID()
	{
		Random random = new Random();
		return random.nextInt(999);
	}
	
	private boolean validateStockInfo()
	{
		if (txtStockID.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the Stock ID!");
			return false;
		}
		else if(txtStockName.getText().trim().isEmpty())
		{			
			JOptionPane.showMessageDialog(null, "Type the Stock Name!");
			return false;
		}
		else if(txtStockOnHand.getText().trim().isEmpty() || !isNumber(txtStockOnHand.getText()))
				{
				JOptionPane.showMessageDialog(null, "Type a proper value for the Stock On Hand!");
				return false;
				}
		else if(txtStockPrice.getText().trim().isEmpty() || !isNumber(txtStockPrice.getText()))
		{
			JOptionPane.showMessageDialog(null, "Type a proper value for the Price!");
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean isNumber(String string)
	{
		try {
			double d = Double.parseDouble(string);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	private boolean validateInvoice()
	{
		if (invoicedTtems.getRowCount() <= 0)
		{
			JOptionPane.showMessageDialog(null, "You have to add at least one item to the invoice!");
			return false;
		}
		else if(txtCliendId.getText().trim().isEmpty())
		{			
			JOptionPane.showMessageDialog(null, "Search for the client of the invoice!");
			return false;
		}
		else {
			return true;
		}
	}
	
	
	private boolean validateClientInfo()
	{
		if (txtEditClientID.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the Client ID!");
			return false;
		}
		else if(txtClientFirstName.getText().trim().isEmpty())
		{			
			JOptionPane.showMessageDialog(null, "Type the Client's First Name!");
			return false;
		}
		else if(txtClientLastName.getText().trim().isEmpty() )
		{
			JOptionPane.showMessageDialog(null, "Type the Client's Last Name!");
			return false;
		}
		else if(txtClientAddress.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the Client Address");
			return false;
		}
		else {
			return true;
		}
	}
	
	private boolean validateUserInfo()
	{
		if (txtUserID.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the User ID!");
			return false;
		}
		else if(txtUserLastName.getText().trim().isEmpty())
		{			
			JOptionPane.showMessageDialog(null, "Type the User's Last Name!");
			return false;
		}
		else if(txtEditUsername.getText().trim().isEmpty() )
		{
			JOptionPane.showMessageDialog(null, "Type the Username!");
			return false;
		}
		else if(txtUserFirstName.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the User's First Name");
			return false;
		}
		else if(new String(txtEditUserPassword.getPassword()).trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Type the User's Password!");
			return false;
		}
		else {
			return true;
		}
	}
}
