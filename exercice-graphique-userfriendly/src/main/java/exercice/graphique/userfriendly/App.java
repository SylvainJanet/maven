package exercice.graphique.userfriendly;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import exercice.metier.bonus.FilmServiceWithGen;
import exercice.metier.bonus.TaskService;
import exercice.metier.bonus.interfaces.IFilmServiceWithGen;
import exercice.metier.bonus.interfaces.ITaskService;
import exercice.modele.Film;
import exercice.modele.Task;
import exercice.tools.CsvToolsGen;

public class App extends JFrame {

	private static final long serialVersionUID = 1875537556826246261L;
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JButton btnNewButton_5;
	private JButton btnNewButton_6;
	private IFilmServiceWithGen filmService = new FilmServiceWithGen();
	private ITaskService taskService = new TaskService();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void refreshFilmList() {
		refreshFilmList(filmService.findAll());
	}

	public void refreshFilmList(List<Film> l) {
		String[] columns = { "Id", "Titre", "Vu", "Note" };
		Object[][] data = new Object[l.size()][4];
		int i = 0;
		for (Film p : l) {
			data[i][0] = p.getId();
			data[i][1] = p.getTitle();
			data[i][2] = p.isWatched();
			data[i][3] = p.getRating();
			i++;
		}
		table.setModel(new DefaultTableModel(data, columns) {
			private static final long serialVersionUID = -1646094382640529576L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { Long.class, String.class, Boolean.class, Integer.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(25);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
	}

	public void refreshTaskList() {
		refreshTaskList(taskService.findAll());

	}

	public void refreshTaskList(List<Task> l) {
		String[] columns = { "Id", "Description", "Fait" };
		Object[][] data = new Object[l.size()][3];
		int i = 0;
		for (Task p : l) {
			data[i][0] = p.getId();
			data[i][1] = p.getDescription();
			data[i][2] = p.isDone();
			i++;
		}
		table_1.setModel(new DefaultTableModel(data, columns) {
			private static final long serialVersionUID = -1646094382640529576L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { Long.class, String.class, Boolean.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
		});
		table_1.getColumnModel().getColumn(0).setPreferredWidth(25);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(200);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(25);
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 20, 20));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 20, 20));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table.getSelectedRow();
				if (pos != -1) {
					btnNewButton_1.setEnabled(true);
					Boolean watched = ((Boolean) table.getValueAt(pos, 2));
					if (watched) {
						btnNewButton_2.setEnabled(false);
						btnNewButton_3.setEnabled(true);
					} else {
						btnNewButton_2.setEnabled(true);
						btnNewButton_3.setEnabled(false);
					}
				} else {
					btnNewButton_1.setEnabled(false);
					btnNewButton_2.setEnabled(false);
					btnNewButton_3.setEnabled(false);
				}
			}
		});

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 25, 25));

		JButton btnNewButton = new JButton("Ajouter film");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = JOptionPane.showInputDialog(App.this, "Titre ?");
				filmService.addNewFilm(title);
				refreshFilmList();

				btnNewButton_1.setEnabled(false);
				btnNewButton_2.setEnabled(false);
				btnNewButton_3.setEnabled(false);
			}
		});
		panel_2.add(btnNewButton);

		btnNewButton_1 = new JButton("Supprimer film");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int pos = table.getSelectedRow();
				if (pos == -1) {
					JOptionPane.showMessageDialog(App.this, "Vous devez selectionner une ligne", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					long idToDel = ((Long) table.getValueAt(pos, 0));
					int res = JOptionPane.showConfirmDialog(App.this, "Etes vous sur de vouloir supprimer ce film : '"
							+ (String) table.getValueAt(pos, 1) + "' ?", "Valider", JOptionPane.OK_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						filmService.delete(idToDel);
						refreshFilmList();
						btnNewButton_1.setEnabled(false);
						btnNewButton_2.setEnabled(false);
						btnNewButton_3.setEnabled(false);
					}
				}
			}
		});
		panel_2.add(btnNewButton_1);

		btnNewButton_2 = new JButton("Film vu");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pos = table.getSelectedRow();
				if (pos == -1) {
					JOptionPane.showMessageDialog(App.this, "Vous devez selectionner une ligne", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					long idWatched = ((Long) table.getValueAt(pos, 0));
					String ratingStr = JOptionPane.showInputDialog(App.this, "Note ?");
					try {
						int rating = Integer.parseInt(ratingStr);
						filmService.watched(idWatched, rating);
						refreshFilmList();
						btnNewButton_1.setEnabled(false);
						btnNewButton_2.setEnabled(false);
						btnNewButton_3.setEnabled(false);

					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(App.this, "La note doit être un nombre entier !", "Erreur !",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		panel_2.add(btnNewButton_2);

		btnNewButton_3 = new JButton("Modifier note");
		btnNewButton_3.setEnabled(false);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pos = table.getSelectedRow();
				if (pos == -1) {
					JOptionPane.showMessageDialog(App.this, "Vous devez selectionner une ligne", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					long idToEdit = ((Long) table.getValueAt(pos, 0));
					String ratingStr = JOptionPane.showInputDialog(App.this, "Note ?");
					try {
						int rating = Integer.parseInt(ratingStr);
						filmService.editRating(idToEdit, rating);
						refreshFilmList();
						btnNewButton_1.setEnabled(false);
						btnNewButton_2.setEnabled(false);
						btnNewButton_3.setEnabled(false);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(App.this, "La note doit être un nombre entier !", "Erreur !",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		panel_2.add(btnNewButton_3);

		JButton btnNewButton_7 = new JButton("Export CSV");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CsvToolsGen.toCsv("films.csv", filmService.findAll(), ",");
					JOptionPane.showMessageDialog(App.this, "Export CSV effectué avec succès", "Export CSV done !",
							JOptionPane.INFORMATION_MESSAGE);
					btnNewButton_1.setEnabled(false);
					btnNewButton_2.setEnabled(false);
					btnNewButton_3.setEnabled(false);
				} catch (IllegalArgumentException | IllegalAccessException | IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(App.this, "Erreur", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_2.add(btnNewButton_7);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 20, 20));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);

		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int pos = table_1.getSelectedRow();
				if (pos != -1) {
					btnNewButton_5.setEnabled(true);
					Boolean done = ((Boolean) table_1.getValueAt(pos, 2));
					if (done) {
						btnNewButton_6.setEnabled(false);
					} else {
						btnNewButton_6.setEnabled(true);
					}
				} else {
					btnNewButton_5.setEnabled(false);
					btnNewButton_6.setEnabled(false);
				}
			}
		});

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 25, 25));

		JButton btnNewButton_4 = new JButton("Ajouter tâche");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String description = JOptionPane.showInputDialog(App.this, "Description ?");
				taskService.addNewTask(description);
				refreshTaskList();
			}
		});
		panel_3.add(btnNewButton_4);

		btnNewButton_5 = new JButton("Supprimer tâche");
		btnNewButton_5.setEnabled(false);
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pos = table_1.getSelectedRow();
				if (pos == -1) {
					JOptionPane.showMessageDialog(App.this, "Vous devez selectionner une ligne", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					long idToDel = ((Long) table_1.getValueAt(pos, 0));
					int res = JOptionPane
							.showConfirmDialog(App.this,
									"Etes vous sur de vouloir supprimer cette tâche : '"
											+ (String) table_1.getValueAt(pos, 1) + "' ?",
									"Valider", JOptionPane.OK_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						taskService.delete(idToDel);
						refreshTaskList();
						btnNewButton_5.setEnabled(false);
						btnNewButton_6.setEnabled(false);
					}
				}
			}
		});
		panel_3.add(btnNewButton_5);

		btnNewButton_6 = new JButton("Tâche effectuée");
		btnNewButton_6.setEnabled(false);
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int pos = table_1.getSelectedRow();
				if (pos == -1) {
					JOptionPane.showMessageDialog(App.this, "Vous devez selectionner une ligne", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					long idDone = ((Long) table_1.getValueAt(pos, 0));

					taskService.done(idDone);
					refreshTaskList();
					btnNewButton_5.setEnabled(false);
					btnNewButton_6.setEnabled(false);

				}
			}
		});
		panel_3.add(btnNewButton_6);

		JButton btnNewButton_8 = new JButton("Export CSV");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CsvToolsGen.toCsv("tasks.csv", taskService.findAll(), ",");
					JOptionPane.showMessageDialog(App.this, "Export CSV effectué avec succès", "Export CSV done !",
							JOptionPane.INFORMATION_MESSAGE);
					btnNewButton_5.setEnabled(false);
					btnNewButton_6.setEnabled(false);
				} catch (IllegalArgumentException | IllegalAccessException | IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(App.this, "Erreur", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnNewButton_8);

		refreshFilmList();
		refreshTaskList();
	}

}
