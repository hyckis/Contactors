// Author: YICHIN HO

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Interface extends JFrame {

	MemberQueries memberQueries = new MemberQueries();
	int memberID;
	String name, type, phone, group;
	int mode;	// 0 -> add; 1 -> update

	// title + search
	JPanel top = new JPanel();	// panel to hold title and search panels
	// title
	JPanel title = new JPanel();	// panel to hold title and add icon
	JLabel titleLabel = new JLabel("Contactors");	// label of title
	JLabel add = new JLabel();	// label of add icon
	ImageIcon addImageIcon = new ImageIcon("add.png");	// img of add icon
	Image addImg = addImageIcon.getImage();
	Image addImage = addImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	Icon addIcon = new ImageIcon(addImage);
	// search
	String keyword;
	JPanel search = new JPanel();
	JTextField searchField = new JTextField();
	JButton searchButton = new JButton("search");

	// display
	JPanel display = new JPanel();
	JTable resultTable = new JTable();

	// adding
	JLabel addLabel = new JLabel();
	// add name
	JPanel addName = new JPanel();
	JLabel addNameLabel = new JLabel("Enter name(required):    ");
	JTextField addNameField = new JTextField();
	// add type
	JPanel addType = new JPanel();
	JLabel addTypeLabel = new JLabel("Choose type:    ");
	String[] types = {"home", "company", "cell"};
	JComboBox<String> addTypeBox = new JComboBox<>(types);
	// add phone
	JPanel addPhone = new JPanel();
	JLabel addPhoneLabel = new JLabel("Enter Phone(required. 9 or 10 nums, start with 0):    ");
	JTextField addPhoneField = new JTextField();
	// add group
	JPanel addGroup = new JPanel();
	JLabel addGroupLabel = new JLabel("Choose group:    ");
	String[] groups = {"classmate", "family", "BFF", "hate"};
	JComboBox<String> addGroupBox = new JComboBox<>(groups);
	JComboBox<String> chooseGroupBox = new JComboBox<>(groups);
	// button for confirm and cancel
	JPanel buttons = new JPanel();
	JButton confirm = new JButton("confirm");
	JButton cancel = new JButton("cancel");

	// deleting and updating
	JButton delete = new JButton("delete");
	JButton update = new JButton("update");
	JButton[] showOnPane = {update, delete};

	public Interface() {

		super("Contators");
		setLayout(new BorderLayout());

		// title
		title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));
		titleLabel.setFont(new Font("Candara", Font.PLAIN, 30));
		add.setIcon(addIcon);
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mode = 0;
				addLabel.setText("Add a new contactor...");
				displayChange();
				addNameField.setText("");
				addTypeBox.setSelectedItem("");
				addPhoneField.setText("");
				addGroupBox.setSelectedIndex(0);
			}
		});
		title.add(titleLabel);
		title.add(Box.createGlue());
		title.add(add);

		// search
		search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));
		// mouse listener for search button
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				keyword = searchField.getText();
				if (!keyword.equals("")) {
					if (memberQueries.getNameTable(keyword).getRowCount() != 0)
						newResultTable(1);
					else if (memberQueries.getPhoneTable(keyword).getRowCount() != 0)
						newResultTable(2);
					else
						JOptionPane.showMessageDialog(null, "no results", "error", 0);
				} else
					JOptionPane.showMessageDialog(null, "invalid search!", "error", 0);
			}
		});
		search.add(searchField);
		search.add(searchButton);

		// top
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		top.add(title);
		top.add(Box.createGlue());
		top.add(search);

		// display
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
		resultTable.setModel(memberQueries.getTableModel());	// set table of name results
		// action listener for group combo box
		chooseGroupBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				group = chooseGroupBox.getSelectedItem().toString();
				if (event.getStateChange() == ItemEvent.SELECTED)
					newResultTable(3);
			}
		});
		// mouse listener for result table
		resultTable.addMouseListener(new MouseAdapter() {	// mouse listener for table rows
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {	// double click
					int row = resultTable.rowAtPoint(e.getPoint());
					name = resultTable.getValueAt(row, 0).toString();
					memberQueries.getAllMember().forEach(m -> {
						if (m.getName().equals(name)) {
							memberID = m.getMemberID();
							type = m.getType();
							phone = m.getPhone();
						}
					});
					JOptionPane.showOptionDialog(null, type+": "+phone, name, 1, 1, null, showOnPane, showOnPane[0]);
				}
			}
		});
		display.add(new JScrollPane(resultTable));

		// adding (can be used on updating too)
		// input name
		addName.setLayout(new BoxLayout(addName, BoxLayout.X_AXIS));
		addNameLabel.setFont(new Font("Candara", Font.PLAIN, 15));
		addNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		addName.add(addNameLabel);
		addName.add(Box.createGlue());
		addName.add(addNameField);
		// choose type
		addType.setLayout(new BoxLayout(addType, BoxLayout.X_AXIS));
		addTypeLabel.setFont(new Font("Candara", Font.PLAIN, 15));
		addTypeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		addType.add(addTypeLabel);
		addType.add(Box.createGlue());
		addType.add(addTypeBox);
		// input phone
		addPhone.setLayout(new BoxLayout(addPhone, BoxLayout.X_AXIS));
		addPhoneLabel.setFont(new Font("Candara", Font.PLAIN, 15));
		addPhoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		addPhone.add(addPhoneLabel);
		addPhone.add(Box.createGlue());
		addPhone.add(addPhoneField);
		// choose group
		addGroup.setLayout(new BoxLayout(addGroup, BoxLayout.X_AXIS));
		addGroupLabel.setFont(new Font("Candara", Font.PLAIN, 15));
		addGroupBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		addGroup.add(addGroupLabel);
		addGroup.add(Box.createGlue());
		addGroup.add(addGroupBox);
		// confirm and cancel button
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(Box.createGlue());
		buttons.add(confirm);
		buttons.add(cancel);
		// mouse listener for confirm button
		confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = addNameField.getText();
				String type = addTypeBox.getSelectedItem().toString();
				String phone = addPhoneField.getText();
				String group = addGroupBox.getSelectedItem().toString();
				if (!name.equals("") && !phone.equals("") && validPhoneInput(type, phone)) {
					if (mode == 0) {	// mode of adding
						memberQueries.addMember(name, type, phone, group);
						JOptionPane.showMessageDialog(null, "successful adding!", "notice", 1);
					} else if (mode == 1) {	// mode of updating
						memberQueries.updateMember(memberID, name, type, phone, group);
						JOptionPane.showMessageDialog(null, "successful updating!", "notice", 1);
					}
					newResultTable(0);	// update new table
				} else	// invalid input
					JOptionPane.showMessageDialog(null, "invalid value, input again", "error", JOptionPane.ERROR_MESSAGE);			
			}
		});
		// mouse listener for cancel button
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newResultTable(0);
			}
		});

		// mouse listener for delete button
		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int opt = JOptionPane.showConfirmDialog(null, "deleting?");
				if (opt == JOptionPane.YES_OPTION) {
					memberQueries.deleteMember(memberID);
					Window win = SwingUtilities.getWindowAncestor(delete);	// find the window that includes delete button
					win.dispose();	// close the window
					JOptionPane.showMessageDialog(null, "successful deleting!", "notice", 1);
					newResultTable(0);
				}
			}
		});

		// mouse listener for update button
		update.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mode = 1;
				addLabel.setText("Update a contactor...");
				add.setEnabled(false);
				displayChange();
				addNameField.setText(name);
				addTypeBox.setSelectedItem(type);
				addPhoneField.setText(phone);
				addGroupBox.setSelectedItem(group);
				Window win = SwingUtilities.getWindowAncestor(update);	// find the window that includes revise button
				win.dispose();	// close the window
			}
		});

		add(top, BorderLayout.NORTH);
		add(display, BorderLayout.CENTER);
		newResultTable(0);

	}

	// change display to displaying mode and read new result table in
	private void newResultTable(int tableType) {
		add.setEnabled(true);
		display.removeAll();
		// sorter for result table
		TableRowSorter<ResultSetTableModel> defaultSorter = new TableRowSorter<ResultSetTableModel>(memberQueries.getTableModel());
		TableRowSorter<ResultSetTableModel> nameSorter = new TableRowSorter<ResultSetTableModel>(memberQueries.getNameTable(keyword));
		TableRowSorter<ResultSetTableModel> phoneSorter = new TableRowSorter<ResultSetTableModel>(memberQueries.getPhoneTable(keyword));
		TableRowSorter<ResultSetTableModel> groupSorter = new TableRowSorter<ResultSetTableModel>(memberQueries.getGroupTable(group));
		if (tableType == 1) {	// result table of searching by name
			resultTable.setModel(memberQueries.getNameTable(keyword));
			resultTable.setRowSorter(nameSorter);
		} else if (tableType == 2) {	// result table of searching by type
			resultTable.setModel(memberQueries.getPhoneTable(keyword));
			resultTable.setRowSorter(phoneSorter);
		} else if (tableType == 3) {
			resultTable.setModel(memberQueries.getGroupTable(group));
			resultTable.setRowSorter(groupSorter);
		} else if (tableType == 0) {	// default result table
			resultTable.setModel(memberQueries.getTableModel());
			resultTable.setRowSorter(defaultSorter);
		}
		display.add(Box.createGlue());
		chooseGroupBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		display.add(chooseGroupBox);
		display.add(Box.createGlue());
		display.add(new JScrollPane(resultTable));
		display.revalidate();
		display.repaint();
	}

	// change display to adding mode or updating mode
	private void displayChange() {
		add.setEnabled(false);
		display.removeAll();
		display.add(Box.createGlue());
		addLabel.setFont(new Font("Candara", Font.PLAIN, 20));
		display.add(addLabel);
		display.add(addName);
		display.add(addType);
		display.add(addPhone);
		display.add(addGroup);
		display.add(Box.createGlue());
		display.add(buttons);
		display.revalidate();
		display.repaint();
	}

	// defensive
	private boolean validPhoneInput(String type, String phone) {
		if (type.equals("cell")) {	// cell type
			return phone.matches("09\\d{8}");	// 09XXXXXXXX
		} else {	// home or company type
			if (phone.matches("0\\d{8}") || phone.matches("0\\d{9}"))	// 0XXXXXXXX or 0XXXXXXXXX
				return true;
			else
				return false;
		}
	}

}
