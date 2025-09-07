//package com.NCByTrain.editor;
//
//import com.NCByTrain.common.BasicConstants;
//import com.NCByTrain.common.City;
//import com.NCByTrain.common.Connection;
//import java.util.Arrays;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.function.BiConsumer;
//
//public class ConnectionsEditor extends JFrame {
//  private final List<Connection> connections;
//  private final BiConsumer<String, Connection> connectionCallback;
//  private final JPanel connectionsPanel;
//
//  public ConnectionsEditor(List<Connection> initialConnections,
//      BiConsumer<String, Connection> callback) {
//    this.connections = new ArrayList<>(initialConnections);
//    this.connectionCallback = callback;
//
//    setTitle("Edit Connections");
//    setSize(400, 600);
//    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//
//    connectionsPanel = new JPanel();
//    connectionsPanel.setLayout(new BoxLayout(connectionsPanel, BoxLayout.Y_AXIS));
//
//    JScrollPane scrollPane = new JScrollPane(connectionsPanel);
//    add(scrollPane);
//
//    refreshConnections();
//  }
//
//  public void addPossibleConnections(List<String> cities) {
//    for (int i = 0; i < cities.size(); i++) {
//      for (int j = i + 1; j < cities.size(); j++) {
//        String city1 = cities.get(i);
//        String city2 = cities.get(j);
//
//        // Check if connection already exists
//        boolean exists = connections.stream().anyMatch(conn ->
//            (conn.getFromTo().get(0).equals(city1) && conn.getFromTo().get(1).equals(city2)) ||
//                (conn.getFromTo().get(0).equals(city2) && conn.getFromTo().get(1).equals(city1))
//        );
//
//        if (!exists) {
//          addConnectionRow(city1, city2, null);
//        }
//      }
//    }
//  }
//
//  private void addConnectionRow(City from, City to, Connection existing) {
//    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
//
//    JLabel label = new JLabel(String.format("between %s and %s", from.name(), to.name()));
//    label.setPreferredSize(new Dimension(150, 25));
//    row.add(label);
//
//    JComboBox<Color> colorCombo = new JComboBox<>(BasicConstants.COLORS.toArray(new Color[0]));
//    colorCombo.setRenderer(new ColorComboRenderer());
//    if (existing != null) {
//      colorCombo.setSelectedItem(existing.getColor());
//    }
//    row.add(colorCombo);
//
//
//    // Convert int[] to Integer[] for the JComboBox
//    Integer[] segmentOptions = Arrays.stream(BasicConstants.SEGMENT_LENGTHS)
//        .boxed()
//        .toArray(Integer[]::new);
//    JComboBox<Integer> segmentCombo = new JComboBox<>(segmentOptions);
//    if (existing != null) {
//      segmentCombo.setSelectedItem(existing.getSegments());
//    }
//    row.add(segmentCombo);
//
//    JButton toggleButton = new JButton(existing != null ? "-" : "+");
//    toggleButton.addActionListener(new ActionListener() {
//      private boolean isAdded = existing != null;
//
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        Color selectedColor = (Color) colorCombo.getSelectedItem();
//        int selectedSegments = (Integer) segmentCombo.getSelectedItem();
//        Connection conn = new Connection(from, to, selectedColor, selectedSegments);
//
//        if (isAdded) {
//          connections.remove(conn);
//          connectionCallback.accept("remove", conn);
//          toggleButton.setText("+");
//          isAdded = false;
//        } else {
//          connections.add(conn);
//          connectionCallback.accept("add", conn);
//          toggleButton.setText("-");
//          isAdded = true;
//        }
//      }
//    });
//    row.add(toggleButton);
//
//    connectionsPanel.add(row);
//  }
//
//  private void refreshConnections() {
//    connectionsPanel.removeAll();
//    for (Connection conn : connections) {
//      addConnectionRow(conn.getFromTo().get(0).name(), conn.getFromTo().get(1).name(), conn);
//    }
//    connectionsPanel.revalidate();
//    connectionsPanel.repaint();
//  }
//
//  public List<Connection> getConnections() {
//    return new ArrayList<>(connections);
//  }
//
//  private static class ColorComboRenderer extends DefaultListCellRenderer {
//    @Override
//    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
//        boolean isSelected, boolean cellHasFocus) {
//      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//      if (value instanceof Color) {
//        Color color = (Color) value;
//        setText(getColorName(color));
//        setBackground(isSelected ? list.getSelectionBackground() : color);
//        setForeground(isSelected ? list.getSelectionForeground() : Color.BLACK);
//      }
//      return this;
//    }
//
//    private String getColorName(Color color) {
//      if (color.equals(Color.RED)) return "Red";
//      if (color.equals(Color.BLUE)) return "Blue";
//      if (color.equals(Color.GREEN)) return "Green";
//      if (color.equals(Color.WHITE)) return "White";
//      return "Unknown";
//    }
//  }
//}