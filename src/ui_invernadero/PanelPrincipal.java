package ui_invernadero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel Principal - Menú Central del Sistema de Invernadero Inteligente
 * Adaptado para Hardware Arduino Real
 * @author Nicom
 */
public class PanelPrincipal extends javax.swing.JFrame {

    private Timer clockTimer;
    // Valores reales basados en el Arduino
    private double temperaturaActual = 25.0;  // Temperatura normal de invernadero
    private int humedadSuelo = 450;           // Valor analógico del sensor (0-1023)
    private int valorLDR = 300;               // Valor LDR (día)
    private boolean sistemaConectado = true;
    
    // Umbrales del Arduino
    private final int UMBRAL_HUMEDAD = 500;   // Umbral humedad suelo
    private final int UMBRAL_LUZ = 500;       // Umbral LDR día/noche
    private final double TEMP_VENTILADOR = 51.0;  // Activar ventilador
    private final double TEMP_ALARMA = 56.0;      // Activar alarma
    
    public PanelPrincipal() {
        initComponents();
        setupCustomComponents();
        startClockTimer();
    }
    
    private void setupCustomComponents() {
        // Configurar ventana principal
        setTitle("🏠 Invernadero Inteligente - Panel de Control Arduino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar fondo
        getContentPane().setBackground(new Color(245, 250, 255));
        
        // Actualizar información inicial
        updateSystemInfo();
        
        // Timer para reloj y simulación de datos de Arduino
        clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
                simulateArduinoData();
            }
        });
    }
    
    private void startClockTimer() {
        clockTimer.start();
    }
    
    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        lblFechaHora.setText(sdf.format(new Date()));
    }
    
    private void simulateArduinoData() {
        // Simular datos reales del Arduino
        temperaturaActual += (Math.random() - 0.5) * 2.0;
        humedadSuelo += (int)((Math.random() - 0.5) * 50);
        valorLDR += (int)((Math.random() - 0.5) * 30);
        
        // Mantener rangos realistas del Arduino
        if (temperaturaActual < 15) temperaturaActual = 15;
        if (temperaturaActual > 60) temperaturaActual = 60;
        if (humedadSuelo < 200) humedadSuelo = 200;
        if (humedadSuelo > 800) humedadSuelo = 800;
        if (valorLDR < 100) valorLDR = 100;
        if (valorLDR > 900) valorLDR = 900;
        
        updateSystemInfo();
    }
    
    private void updateSystemInfo() {
        // Mostrar temperatura
        lblTemperatura.setText(String.format("🌡️ %.1f°C", temperaturaActual));
        
        // Convertir humedad analógica a porcentaje (invertido)
        int humedadPorcentaje = (int)map(humedadSuelo, 1023, 0, 0, 100);
        lblHumedad.setText(String.format("💧 %d%% (%d)", humedadPorcentaje, humedadSuelo));
        
        // Estado de iluminación
        String estadoLuz = valorLDR >= UMBRAL_LUZ ? "🌙 Noche" : "☀️ Día";
        lblLuminosidad.setText(String.format("%s (%d)", estadoLuz, valorLDR));
        
        // Estado de conexión
        lblEstadoConexion.setText("🔗 " + (sistemaConectado ? "ARDUINO CONECTADO" : "DESCONECTADO"));
        lblEstadoConexion.setForeground(sistemaConectado ? new Color(0, 150, 0) : Color.RED);
        
        // Alertas del sistema
        updateAlertas();
    }
    
    private void updateAlertas() {
        String alertas = "<html><b>🚨 ESTADO DEL SISTEMA:</b><br>";
        
        // Alertas de temperatura
        if (temperaturaActual > TEMP_ALARMA) {
            alertas += "<font color='red'>⚠️ TEMPERATURA CRÍTICA - BUZZER ACTIVO</font><br>";
        } else if (temperaturaActual > TEMP_VENTILADOR) {
            alertas += "<font color='orange'>🌀 VENTILADOR ACTIVO</font><br>";
        } else {
            alertas += "<font color='green'>✅ Temperatura normal</font><br>";
        }
        
        // Alertas de humedad
        if (humedadSuelo >= UMBRAL_HUMEDAD) {
            alertas += "<font color='blue'>💧 VÁLVULA DE RIEGO ACTIVA</font><br>";
        } else {
            alertas += "<font color='brown'>🏜️ Suelo seco - Sin riego</font><br>";
        }
        
        // Estado de iluminación
        if (valorLDR >= UMBRAL_LUZ) {
            alertas += "<font color='purple'>💡 LED NOCTURNO ENCENDIDO</font><br>";
        } else {
            alertas += "<font color='gray'>☀️ Iluminación natural</font><br>";
        }
        
        alertas += "</html>";
        lblAlertas.setText(alertas);
    }
    
    // Función para mapear valores (como en Arduino)
    private double map(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        lblTituloPrincipal = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelInformacionArduino = new javax.swing.JPanel();
        lblFechaHora = new javax.swing.JLabel();
        lblTemperatura = new javax.swing.JLabel();
        lblHumedad = new javax.swing.JLabel();
        lblLuminosidad = new javax.swing.JLabel();
        lblEstadoConexion = new javax.swing.JLabel();
        panelNavegacion = new javax.swing.JPanel();
        btnControlManual = new javax.swing.JButton();
        btnControlAutomatico = new javax.swing.JButton();
        btnMonitoreoSerial = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        panelAlertas = new javax.swing.JPanel();
        lblAlertas = new javax.swing.JLabel();
        panelUmbralesArduino = new javax.swing.JPanel();
        lblTituloUmbrales = new javax.swing.JLabel();
        lblUmbralTemp = new javax.swing.JLabel();
        lblUmbralHumedad = new javax.swing.JLabel();
        lblUmbralLuz = new javax.swing.JLabel();
        iconoPrincipal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 750));

        panelPrincipal.setBackground(new java.awt.Color(245, 250, 255));
        panelPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTituloPrincipal.setFont(new java.awt.Font("Arial", 1, 32));
        lblTituloPrincipal.setForeground(new java.awt.Color(0, 100, 150));
        lblTituloPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloPrincipal.setText("🤖 INVERNADERO INTELIGENTE ARDUINO 🌱");

        lblSubtitulo.setFont(new java.awt.Font("Arial", 1, 16));
        lblSubtitulo.setForeground(new java.awt.Color(50, 100, 150));
        lblSubtitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubtitulo.setText("Sistema de Control con Sensores LM35 • Humedad • LDR • Actuadores Reales");

        panelInformacionArduino.setBackground(new java.awt.Color(230, 240, 250));
        panelInformacionArduino.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "📡 Datos en Tiempo Real del Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 100, 150)));

        lblFechaHora.setFont(new java.awt.Font("Arial", 1, 14));
        lblFechaHora.setForeground(new java.awt.Color(100, 100, 100));
        lblFechaHora.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFechaHora.setText("📅 --/--/---- - --:--:--");

        lblTemperatura.setFont(new java.awt.Font("Arial", 1, 16));
        lblTemperatura.setForeground(new java.awt.Color(200, 50, 50));
        lblTemperatura.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemperatura.setText("🌡️ 25.0°C");

        lblHumedad.setFont(new java.awt.Font("Arial", 1, 16));
        lblHumedad.setForeground(new java.awt.Color(50, 150, 200));
        lblHumedad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHumedad.setText("💧 45% (450)");

        lblLuminosidad.setFont(new java.awt.Font("Arial", 1, 16));
        lblLuminosidad.setForeground(new java.awt.Color(255, 150, 0));
        lblLuminosidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLuminosidad.setText("☀️ Día (300)");

        lblEstadoConexion.setFont(new java.awt.Font("Arial", 1, 14));
        lblEstadoConexion.setForeground(new java.awt.Color(0, 150, 0));
        lblEstadoConexion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstadoConexion.setText("🔗 ARDUINO CONECTADO");

        javax.swing.GroupLayout panelInformacionArduinoLayout = new javax.swing.GroupLayout(panelInformacionArduino);
        panelInformacionArduino.setLayout(panelInformacionArduinoLayout);
        panelInformacionArduinoLayout.setHorizontalGroup(
            panelInformacionArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionArduinoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInformacionArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFechaHora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstadoConexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelInformacionArduinoLayout.createSequentialGroup()
                        .addComponent(lblTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblLuminosidad, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelInformacionArduinoLayout.setVerticalGroup(
            panelInformacionArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionArduinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFechaHora)
                .addGap(18, 18, 18)
                .addGroup(panelInformacionArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTemperatura)
                    .addComponent(lblHumedad)
                    .addComponent(lblLuminosidad))
                .addGap(18, 18, 18)
                .addComponent(lblEstadoConexion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelNavegacion.setBackground(new java.awt.Color(240, 245, 255));
        panelNavegacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "🎛️ Panel de Control", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 100, 150)));

        btnControlManual.setBackground(new java.awt.Color(100, 200, 255));
        btnControlManual.setFont(new java.awt.Font("Arial", 1, 14));
        btnControlManual.setText("🎛️ CONTROL MANUAL");
        btnControlManual.setPreferredSize(new java.awt.Dimension(200, 45));
        btnControlManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlManualActionPerformed(evt);
            }
        });

        btnControlAutomatico.setBackground(new java.awt.Color(100, 255, 150));
        btnControlAutomatico.setFont(new java.awt.Font("Arial", 1, 14));
        btnControlAutomatico.setText("🤖 CONTROL AUTOMÁTICO");
        btnControlAutomatico.setPreferredSize(new java.awt.Dimension(200, 45));
        btnControlAutomatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlAutomaticoActionPerformed(evt);
            }
        });

        btnMonitoreoSerial.setBackground(new java.awt.Color(255, 200, 100));
        btnMonitoreoSerial.setFont(new java.awt.Font("Arial", 1, 14));
        btnMonitoreoSerial.setText("📊 MONITOR SERIAL");
        btnMonitoreoSerial.setPreferredSize(new java.awt.Dimension(200, 45));
        btnMonitoreoSerial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitoreoSerialActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(255, 150, 150));
        btnSalir.setFont(new java.awt.Font("Arial", 1, 14));
        btnSalir.setText("🚪 SALIR");
        btnSalir.setPreferredSize(new java.awt.Dimension(200, 45));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNavegacionLayout = new javax.swing.GroupLayout(panelNavegacion);
        panelNavegacion.setLayout(panelNavegacionLayout);
        panelNavegacionLayout.setHorizontalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavegacionLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnControlManual, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(btnMonitoreoSerial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnControlAutomatico, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelNavegacionLayout.setVerticalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavegacionLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnControlManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnControlAutomatico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMonitoreoSerial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelAlertas.setBackground(new java.awt.Color(255, 250, 240));
        panelAlertas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "🚨 Estado de Actuadores Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(200, 100, 50)));

        lblAlertas.setFont(new java.awt.Font("Arial", 0, 12));
        lblAlertas.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblAlertas.setText("<html><b>🚨 ESTADO DEL SISTEMA:</b><br>Inicializando...</html>");

        javax.swing.GroupLayout panelAlertasLayout = new javax.swing.GroupLayout(panelAlertas);
        panelAlertas.setLayout(panelAlertasLayout);
        panelAlertasLayout.setHorizontalGroup(
            panelAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelAlertasLayout.setVerticalGroup(
            panelAlertasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlertasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelUmbralesArduino.setBackground(new java.awt.Color(240, 255, 240));
        panelUmbralesArduino.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "⚙️ Umbrales Configurados en Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 150, 50)));

        lblTituloUmbrales.setFont(new java.awt.Font("Arial", 1, 12));
        lblTituloUmbrales.setForeground(new java.awt.Color(50, 100, 50));
        lblTituloUmbrales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloUmbrales.setText("📊 PARÁMETROS DEL SISTEMA");

        lblUmbralTemp.setFont(new java.awt.Font("Arial", 0, 11));
        lblUmbralTemp.setForeground(new java.awt.Color(150, 50, 50));
        lblUmbralTemp.setText("🌡️ Temperatura: ≤50°C (Normal) | 51-55°C (Ventilador) | >56°C (Alarma)");

        lblUmbralHumedad.setFont(new java.awt.Font("Arial", 0, 11));
        lblUmbralHumedad.setForeground(new java.awt.Color(50, 100, 150));
        lblUmbralHumedad.setText("💧 Humedad Suelo: <500 (Seco) | ≥500 (Activar Riego)");

        lblUmbralLuz.setFont(new java.awt.Font("Arial", 0, 11));
        lblUmbralLuz.setForeground(new java.awt.Color(200, 150, 0));
        lblUmbralLuz.setText("☀️ Luminosidad: <500 (Día) | ≥500 (Noche - Activar LED)");

        javax.swing.GroupLayout panelUmbralesArduinoLayout = new javax.swing.GroupLayout(panelUmbralesArduino);
        panelUmbralesArduino.setLayout(panelUmbralesArduinoLayout);
        panelUmbralesArduinoLayout.setHorizontalGroup(
            panelUmbralesArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesArduinoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUmbralesArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloUmbrales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUmbralTemp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUmbralHumedad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUmbralLuz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelUmbralesArduinoLayout.setVerticalGroup(
            panelUmbralesArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesArduinoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloUmbrales)
                .addGap(18, 18, 18)
                .addComponent(lblUmbralTemp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUmbralHumedad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUmbralLuz)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        iconoPrincipal.setFont(new java.awt.Font("Segoe UI Emoji", 0, 48));
        iconoPrincipal.setForeground(new java.awt.Color(50, 150, 50));
        iconoPrincipal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconoPrincipal.setText("🏠🤖🌿");

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTituloPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSubtitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNavegacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelUmbralesArduino, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelInformacionArduino, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(iconoPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(25, 25, 25))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTituloPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelInformacionArduino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconoPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panelNavegacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelUmbralesArduino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void btnControlManualActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        try {
            new ControlManual().setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir Control Manual: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                 

    private void btnControlAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        try {
            new ControlAutomatico().setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al abrir Control Automático: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                                     

    private void btnMonitoreoSerialActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        JOptionPane.showMessageDialog(this, 
            "📊 MONITOR SERIAL ARDUINO\n\n" +
            "Para monitorear datos en tiempo real:\n\n" +
            "1. Conectar Arduino por USB\n" +
            "2. Abrir Arduino IDE → Herramientas → Monitor Serial\n" +
            "3. Configurar velocidad: 9600 baud\n" +
            "4. Observar datos de sensores:\n" +
            "   • Temperatura LM35 (Pin A0)\n" +
            "   • Humedad Suelo (Pin A1)\n" +
            "   • LDR Luminosidad (Pin A2)\n\n" +
            "Esta función será integrada en futuras versiones.", 
            "Monitor Serial Arduino", 
            JOptionPane.INFORMATION_MESSAGE);
    }                                                  

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea salir del sistema?\n\nEsto cerrará la comunicación con Arduino.", 
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Detener timers antes de salir
            if (clockTimer != null) {
                clockTimer.stop();
            }
            System.exit(0);
        }
    }                                        

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PanelPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PanelPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnControlAutomatico;
    private javax.swing.JButton btnControlManual;
    private javax.swing.JButton btnMonitoreoSerial;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel iconoPrincipal;
    private javax.swing.JLabel lblAlertas;
    private javax.swing.JLabel lblEstadoConexion;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblHumedad;
    private javax.swing.JLabel lblLuminosidad;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTemperatura;
    private javax.swing.JLabel lblTituloUmbrales;
    private javax.swing.JLabel lblTituloPrincipal;
    private javax.swing.JLabel lblUmbralHumedad;
    private javax.swing.JLabel lblUmbralLuz;
    private javax.swing.JLabel lblUmbralTemp;
    private javax.swing.JPanel panelAlertas;
    private javax.swing.JPanel panelInformacionArduino;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelUmbralesArduino;
    // End of variables declaration                   
}