package ui_invernadero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pantalla de Bienvenida - Sistema de Invernadero Inteligente
 * Compatible con Arduino: LM35, Sensor Humedad, LDR, Actuadores
 * @author Nicom
 */
public class PantallaBienvenida extends javax.swing.JFrame {
    
    private Timer timer;
    private int progress = 0;
    
    public PantallaBienvenida() {
        initComponents();
        setupCustomComponents();
        startProgressBar();
    }
    
    private void setupCustomComponents() {
        // Configurar ventana
        setTitle("🏠 Invernadero Inteligente - Iniciando Sistema Arduino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar fondo
        getContentPane().setBackground(new Color(245, 255, 250));
        
        // Timer para simular carga del sistema
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 2;
                progressBar.setValue(progress);
                
                // Actualizar mensaje según progreso
                if (progress < 30) {
                    progressBar.setString("Inicializando Arduino...");
                } else if (progress < 60) {
                    progressBar.setString("Conectando sensores LM35, Humedad, LDR...");
                } else if (progress < 90) {
                    progressBar.setString("Configurando actuadores...");
                } else {
                    progressBar.setString("Sistema listo!");
                }
                
                if (progress >= 100) {
                    timer.stop();
                    // Esperar 1 segundo y abrir panel principal
                    Timer delayTimer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            abrirPanelPrincipal();
                            ((Timer)e.getSource()).stop();
                        }
                    });
                    delayTimer.start();
                }
            }
        });
    }
    
    private void startProgressBar() {
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("Iniciando sistema...");
        timer.start();
    }
    
    private void abrirPanelPrincipal() {
        try {
            new PanelPrincipal().setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al inicializar el sistema: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        progressBar = new javax.swing.JProgressBar();
        lblCargando = new javax.swing.JLabel();
        iconoInvernadero = new javax.swing.JLabel();
        panelEspecificaciones = new javax.swing.JPanel();
        lblEspecsHardware = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 700));

        jPanel1.setBackground(new java.awt.Color(245, 255, 250));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 32));
        lblTitulo.setForeground(new java.awt.Color(34, 139, 34));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("🌱 INVERNADERO INTELIGENTE 🌱");

        lblSubtitulo.setFont(new java.awt.Font("Arial", 1, 18));
        lblSubtitulo.setForeground(new java.awt.Color(60, 120, 60));
        lblSubtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitulo.setText("Sistema de Monitoreo y Control con Arduino");

        txtDescripcion.setEditable(false);
        txtDescripcion.setBackground(new java.awt.Color(240, 248, 240));
        txtDescripcion.setColumns(20);
        txtDescripcion.setFont(new java.awt.Font("Arial", 0, 14));
        txtDescripcion.setForeground(new java.awt.Color(51, 51, 51));
        txtDescripcion.setRows(5);
        txtDescripcion.setText("🤖 SISTEMA INVERNADERO INTELIGENTE CON ARDUINO 🤖\n\nEste sistema integra sensores reales conectados a Arduino para crear\nun ambiente óptimo y automatizado para el crecimiento de plantas.\n\n📡 SENSORES IMPLEMENTADOS:\n• Sensor de Temperatura LM35 (Precisión ±0.5°C)\n• Sensor de Humedad del Suelo (Medición analógica)\n• Sensor LDR de Luminosidad (Detección día/noche)\n\n⚙️ ACTUADORES CONTROLADOS:\n• Ventilador de Refrigeración (Control automático)\n• Sistema de Riego con Válvula (Activación por humedad)\n• Iluminación LED (Control por luminosidad)\n• Servo Motor para Ventilación Extra\n• Buzzer de Alertas (Temperaturas críticas)\n\n🎯 CARACTERÍSTICAS:\n• Monitoreo en tiempo real\n• Control manual y automático\n• Umbrales configurables\n• Interfaz gráfica intuitiva");
        txtDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Descripción del Sistema", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(34, 139, 34)));
        jScrollPane1.setViewportView(txtDescripcion);

        progressBar.setForeground(new java.awt.Color(34, 139, 34));
        progressBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(34, 139, 34)));

        lblCargando.setFont(new java.awt.Font("Arial", 1, 14));
        lblCargando.setForeground(new java.awt.Color(34, 139, 34));
        lblCargando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCargando.setText("Inicializando componentes del sistema Arduino...");

        iconoInvernadero.setFont(new java.awt.Font("Segoe UI Emoji", 0, 64));
        iconoInvernadero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconoInvernadero.setText("🏠🌿🤖");

        panelEspecificaciones.setBackground(new java.awt.Color(235, 245, 235));
        panelEspecificaciones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Especificaciones Hardware", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(34, 139, 34)));

        lblEspecsHardware.setFont(new java.awt.Font("Arial", 0, 12));
        lblEspecsHardware.setForeground(new java.awt.Color(60, 60, 60));
        lblEspecsHardware.setText("<html><b>🔧 CONFIGURACIÓN ARDUINO:</b><br>• Temperatura (LM35): Pin A0 - Umbrales: ≤50°C, 51-55°C, >56°C<br>• Humedad Suelo: Pin A1 - Umbral: 500 (analógico)<br>• LDR Luminosidad: Pin A2 - Umbral: 500 (día/noche)<br>• Ventilador: Pin 8 | Buzzer: Pin 13 | LED: Pin 11<br>• Válvula Riego: Pin 12 | Servo: Pin 7 | Botón: Pin 6</html>");

        javax.swing.GroupLayout panelEspecificacionesLayout = new javax.swing.GroupLayout(panelEspecificaciones);
        panelEspecificaciones.setLayout(panelEspecificacionesLayout);
        panelEspecificacionesLayout.setHorizontalGroup(
            panelEspecificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEspecificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEspecsHardware, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelEspecificacionesLayout.setVerticalGroup(
            panelEspecificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEspecificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEspecsHardware, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSubtitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCargando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconoInvernadero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEspecificaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(iconoInvernadero, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelEspecificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(lblCargando)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PantallaBienvenida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaBienvenida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaBienvenida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaBienvenida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaBienvenida().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel iconoInvernadero;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCargando;
    private javax.swing.JLabel lblEspecsHardware;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelEspecificaciones;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea txtDescripcion;
    // End of variables declaration                   
}