import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageUserUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load User");
    public JButton btnSave = new JButton("Save User");
    public JButton btnDelete = new JButton("Delete Existing User");

    public String types[] = {"Customer", "Cashier", "Manager", "Administrator"};

    public JTextField txtUsername = new JTextField(20);
    public JTextField txtPassword = new JTextField(20);
    public JTextField txtFullName = new JTextField(20);
    public JComboBox boxUserType = new JComboBox(types);



    public ManageUserUI() {
        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Manage System Users");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        panelButtons.add(btnDelete);
        view.getContentPane().add(panelButtons);

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("UserName "));
        line1.add(txtUsername);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Password "));
        line2.add(txtPassword);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Full Name "));
        line3.add(txtFullName);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("User Type "));
        line4.add(boxUserType);
        view.getContentPane().add(line4);


        btnLoad.addActionListener(new LoadButtonListerner());

        btnSave.addActionListener(new SaveButtonListener());

    }

    public void run() {
        view.setVisible(true);
    }

    class LoadButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Gson gson = new Gson();
            String un = txtUsername.getText();

            if (un.length() == 0) {
                JOptionPane.showMessageDialog(null, "UserName cannot be null!");
                return;
            }

            try {

                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_USER;
                msg.data = un;

//                msg = StoreManager.getInstance().getNetworkAdapter().send(msg, "localhost", 1000);

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "User does NOT exist!");
                }
                else {
                    UserModel user = gson.fromJson(msg.data, UserModel.class);
                    txtUsername.setText(user.mUsername);
                    txtPassword.setText(user.mPassword);
                    txtFullName.setText(user.mFullname);
                    boxUserType.setSelectedItem(user.mUserType);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();
            Gson gson = new Gson();
            String un = txtUsername.getText();

            if (un.length() == 0) {
                JOptionPane.showMessageDialog(null, "UserName cannot be null!");
                return;
            }


            String pass = txtPassword.getText();
            if (pass.length() == 0) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                return;
            }

            user.mPassword = pass;

            String fullName = txtFullName.getText();
            if (fullName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty!");
                return;
            }

            user.mFullname = fullName;

            String userType = (String) boxUserType.getSelectedItem();
            if (userType == "Customer") {
                user.mUserType = 0;
            }
            else if (userType == "Cashier") {
                user.mUserType = 1;
            }
            else if (userType == "Manager") {
                user.mUserType = 2;
            }
            else if (userType == "ADMIN")  {
                user.mUserType = 3;
            }

            try {
                MessageModel msg = new MessageModel();
                msg.code = MessageModel.PUT_USER;
                msg.data = gson.toJson(user);

//                msg = StoreManager.getInstance().getNetworkAdapter().send(msg, "localhost", 1000);

                if (msg.code == MessageModel.OPERATION_FAILED)
                    JOptionPane.showMessageDialog(null, "User NOT added successfully");
                else
                    JOptionPane.showMessageDialog(null, "User added successfully!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}