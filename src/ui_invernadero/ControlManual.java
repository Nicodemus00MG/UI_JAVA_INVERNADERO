package ui_invernadero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Control Manual - Interfaz para Arduino Real
 * Sensores: LM35(A0), Humedad(A1), LDR(A2)
 * Actuadores: Ventilador(8), LED(11), Válvula(12), Servo(7), Buzzer(13)
 * @author Nicom
 */
public class ControlManual extends javax.swing.JFrame {
    
    private Timer updateTimer;
    
    // Variables del Arduino - Valores reales simulados
    private double temperaturaLM35 = 25.0;        // Sensor LM35 (Pin A0)
    private int humedadAnalogica = 450;           // Sensor humedad (Pin A1) 0-1023
    private int valorLDR = 300;                   // Sensor LDR (Pin A2) 0-1023
    
    // Estados de actuadores (Pines digitales Arduino)
    private boolean ventiladorActivo = false;    // Pin 8
    private boolean buzzerActivo = false;         // Pin 13  
    private boolean ledActivo = false;            // Pin 11
    private boolean valvulaActiva = false;        // Pin 12
    private boolean servoAbierto = false;         // Pin 7 (0° o 90°)
    
    // Umbrales del Arduino (exactos del código que me mostraste)
    private final int UMBRAL_HUMEDAD = 500;       // Arduino: <500 (seco), >=500 (húmedo)
    private final int UMBRAL_LUZ = 500;           // Arduino: <500 (día), >=500 (noche)
    private final double TEMP_NORMAL = 50.0;      // Arduino: <=50°C (normal)
    private final double TEMP_VENTILADOR = 51.0;  // Arduino: 51-55°C (ventilador)
    private final double TEMP_BUZZER = 56.0;      // Arduino: >56°C (buzzer)
    
    public ControlManual() {
        initComponents();
        setupCustomComponents();
        startUpdateTimer();
    }
    
    private void setupCustomComponents() {
        // Configurar ventana
        setTitle("🎛️ Control Manual - Arduino Invernadero Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar fondo
        getContentPane().setBackground(new Color(240, 248, 255));
        
        // Actualizar displays iniciales
        updateDisplays();
        
        // Timer para actualizar información cada segundo
        updateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDateTime();
                simularSensoresArduino();
                verificarUmbralesArduino();
            }
        });
    }
    
    private void startUpdateTimer() {
        updateTimer.start();
    }
    
    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblFechaHora.setText("📅 " + sdf.format(new Date()));
    }
    
    private void simularSensoresArduino() {
        // Simular variaciones reales de los sensores Arduino
        temperaturaLM35 += (Math.random() - 0.5) * 1.5;
        humedadAnalogica += (int)((Math.random() - 0.5) * 40);
        valorLDR += (int)((Math.random() - 0.5) * 30);
        
        // Mantener rangos realistas del Arduino
        if (temperaturaLM35 < 15) temperaturaLM35 = 15;
        if (temperaturaLM35 > 70) temperaturaLM35 = 70;
        if (humedadAnalogica < 200) humedadAnalogica = 200;
        if (humedadAnalogica > 800) humedadAnalogica = 800;
        if (valorLDR < 100) valorLDR = 100;
        if (valorLDR > 900) valorLDR = 900;
        
        updateDisplays();
    }
    
    private void verificarUmbralesArduino() {
        // Mostrar alertas según umbrales EXACTOS del Arduino
        String alertas = "<html><b>🚨 ALERTAS BASADAS EN CÓDIGO ARDUINO:</b><br>";
        
        // Temperatura (LM35) - Umbrales exactos del Arduino
        if (temperaturaLM35 > TEMP_BUZZER) {
            alertas += "<font color='red'>🔥 TEMPERATURA >56°C - BUZZER + VENTILADOR ACTIVOS</font><br>";
        } else if (temperaturaLM35 > TEMP_VENTILADOR) {
            alertas += "<font color='orange'>🌡️ TEMPERATURA 51-55°C - VENTILADOR ACTIVO</font><br>";
        } else if (temperaturaLM35 <= TEMP_NORMAL) {
            alertas += "<font color='green'>✅ TEMPERATURA ≤50°C - NORMAL</font><br>";
        }
        
        // Humedad del suelo - Umbral exacto del Arduino
        if (humedadAnalogica >= UMBRAL_HUMEDAD) {
            alertas += "<font color='blue'>💧 HUMEDAD ≥500 - VÁLVULA ACTIVA</font><br>";
        } else {
            alertas += "<font color='brown'>🏜️ HUMEDAD <500 - VÁLVULA INACTIVA</font><br>";
        }
        
        // Luminosidad (LDR) - Umbral exacto del Arduino
        if (valorLDR >= UMBRAL_LUZ) {
            alertas += "<font color='purple'>🌙 LDR ≥500 - NOCHE - LED ACTIVO</font><br>";
        } else {
            alertas += "<font color='gold'>☀️ LDR <500 - DÍA - LED INACTIVO</font><br>";
        }
        
        alertas += "</html>";
        lblAlertas.setText(alertas);
    }
    
    private void updateDisplays() {
        // Actualizar sensores con formato Arduino
        lblTemperaturaLM35.setText(String.format("🌡️ LM35: %.1f°C (Pin A0)", temperaturaLM35));
        
        // Mostrar tanto porcentaje como valor analógico (como en Arduino)
        int humedadPorcentaje = (int)map(humedadAnalogica, 1023, 0, 0, 100);
        lblHumedadSuelo.setText(String.format("💧 Humedad: %d%% (%d) (Pin A1)", humedadPorcentaje, humedadAnalogica));
        
        // Estado LDR con umbral Arduino
        String estadoLuz = valorLDR >= UMBRAL_LUZ ? "🌙 Noche" : "☀️ Día";
        lblLuminosidad.setText(String.format("☀️ LDR: %s (%d) (Pin A2)", estadoLuz, valorLDR));
        
        // Actualizar barras de progreso
        progressTemperatura.setValue((int)temperaturaLM35);
        progressTemperatura.setString(String.format("%.1f°C", temperaturaLM35));
        
        progressHumedad.setValue(humedadPorcentaje);
        progressHumedad.setString(String.format("%d%% (%d)", humedadPorcentaje, humedadAnalogica));
        
        int luzPorcentaje = (int)map(valorLDR, 0, 1023, 0, 100);
        progressLuz.setValue(luzPorcentaje);
        progressLuz.setString(valorLDR >= UMBRAL_LUZ ? "🌙 Noche" : "☀️ Día");
        
        // Actualizar colores de botones según estado
        btnVentilador.setBackground(ventiladorActivo ? new Color(100, 255, 100) : Color.LIGHT_GRAY);
        btnVentilador.setText(ventiladorActivo ? "🌀 Ventilador ON (Pin 8)" : "🌀 Ventilador OFF (Pin 8)");
        
        btnBuzzer.setBackground(buzzerActivo ? new Color(255, 100, 100) : Color.LIGHT_GRAY);
        btnBuzzer.setText(buzzerActivo ? "🚨 Buzzer ON (Pin 13)" : "🚨 Buzzer OFF (Pin 13)");
        
        btnLED.setBackground(ledActivo ? new Color(255, 255, 100) : Color.LIGHT_GRAY);
        btnLED.setText(ledActivo ? "💡 LED ON (Pin 11)" : "💡 LED OFF (Pin 11)");
        
        btnValvula.setBackground(valvulaActiva ? new Color(100, 150, 255) : Color.LIGHT_GRAY);
        btnValvula.setText(valvulaActiva ? "💧 Válvula ON (Pin 12)" : "💧 Válvula OFF (Pin 12)");
        
        btnServo.setBackground(servoAbierto ? new Color(150, 255, 200) : Color.LIGHT_GRAY);
        btnServo.setText(servoAbierto ? "🚪 Servo 90° (Pin 7)" : "🚪 Servo 0° (Pin 7)");
    }
    
    // Función map idéntica a Arduino
    private double map(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelSensoresArduino = new javax.swing.JPanel();
        lblTemperaturaLM35 = new javax.swing.JLabel();
        progressTemperatura = new javax.swing.JProgressBar();
        lblHumedadSuelo = new javax.swing.JLabel();
        progressHumedad = new javax.swing.JProgressBar();
        lblLuminosidad = new javax.swing.JLabel();
        progressLuz = new javax.swing.JProgressBar();
        panelActuadoresArduino = new javax.swing.JPanel();
        btnVentilador = new javax.swing.JButton();
        btnBuzzer = new javax.swing.JButton();
        btnLED = new javax.swing.JButton();
        btnValvula = new javax.swing.JButton();
        btnServo = new javax.swing.JButton();
        panelAlertas = new javax.swing.JPanel();
        lblAlertas = new javax.swing.JLabel();
        panelUmbralesInfo = new javax.swing.JPanel();
        lblInfoUmbrales = new javax.swing.JLabel();
        panelNavegacion = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnControlAutomatico = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblFechaHora = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1150, 800));

        panelPrincipal.setBackground(new java.awt.Color(240, 248, 255));
        panelPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 26));
        lblTitulo.setForeground(new java.awt.Color(0, 100, 150));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("🎛️ CONTROL MANUAL - ARDUINO INVERNADERO 🌱");

        panelSensoresArduino.setBackground(new java.awt.Color(230, 240, 250));
        panelSensoresArduino.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "📡 Sensores Arduino en Tiempo Real", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(0, 100, 150)));

        lblTemperaturaLM35.setFont(new java.awt.Font("Arial", 1, 14));
        lblTemperaturaLM35.setForeground(new java.awt.Color(200, 50, 50));
        lblTemperaturaLM35.setText("🌡️ LM35: 25.0°C (Pin A0)");

        progressTemperatura.setMaximum(70);
        progressTemperatura.setValue(25);
        progressTemperatura.setStringPainted(true);
        progressTemperatura.setString("25.0°C");
        progressTemperatura.setForeground(new java.awt.Color(255, 100, 100));

        lblHumedadSuelo.setFont(new java.awt.Font("Arial", 1, 14));
        lblHumedadSuelo.setForeground(new java.awt.Color(50, 150, 200));
        lblHumedadSuelo.setText("💧 Humedad: 45% (450) (Pin A1)");

        progressHumedad.setValue(45);
        progressHumedad.setStringPainted(true);
        progressHumedad.setString("45% (450)");
        progressHumedad.setForeground(new java.awt.Color(100, 150, 255));

        lblLuminosidad.setFont(new java.awt.Font("Arial", 1, 14));
        lblLuminosidad.setForeground(new java.awt.Color(255, 150, 0));
        lblLuminosidad.setText("☀️ LDR: Día (300) (Pin A2)");

        progressLuz.setValue(30);
        progressLuz.setStringPainted(true);
        progressLuz.setString("☀️ Día");
        progressLuz.setForeground(new java.awt.Color(255, 200, 100));

        javax.swing.GroupLayout panelSensoresArduinoLayout = new javax.swing.GroupLayout(panelSensoresArduino);
        panelSensoresArduino.setLayout(panelSensoresArduinoLayout);
        panelSensoresArduinoLayout.setHorizontalGroup(
            panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSensoresArduinoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTemperaturaLM35, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHumedadSuelo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLuminosidad, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressLuz, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSensoresArduinoLayout.setVerticalGroup(
            panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSensoresArduinoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTemperaturaLM35)
                    .addComponent(lblHumedadSuelo)
                    .addComponent(lblLuminosidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSensoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressLuz, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelActuadoresArduino.setBackground(new java.awt.Color(240, 255, 240));
        panelActuadoresArduino.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "⚙️ Control Manual de Actuadores Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 150, 50)));

        btnVentilador.setFont(new java.awt.Font("Arial", 1, 12));
        btnVentilador.setText("🌀 Ventilador OFF (Pin 8)");
        btnVentilador.setPreferredSize(new java.awt.Dimension(200, 45));
        btnVentilador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentiladorActionPerformed(evt);
            }
        });

        btnBuzzer.setFont(new java.awt.Font("Arial", 1, 12));
        btnBuzzer.setText("🚨 Buzzer OFF (Pin 13)");
        btnBuzzer.setPreferredSize(new java.awt.Dimension(200, 45));
        btnBuzzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuzzerActionPerformed(evt);
            }
        });

        btnLED.setFont(new java.awt.Font("Arial", 1, 12));
        btnLED.setText("💡 LED OFF (Pin 11)");
        btnLED.setPreferredSize(new java.awt.Dimension(200, 45));
        btnLED.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLEDActionPerformed(evt);
            }
        });

        btnValvula.setFont(new java.awt.Font("Arial", 1, 12));
        btnValvula.setText("💧 Válvula OFF (Pin 12)");
        btnValvula.setPreferredSize(new java.awt.Dimension(200, 45));
        btnValvula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValvulaActionPerformed(evt);
            }
        });

        btnServo.setFont(new java.awt.Font("Arial", 1, 12));
        btnServo.setText("🚪 Servo 0° (Pin 7)");
        btnServo.setPreferredSize(new java.awt.Dimension(200, 45));
        btnServo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelActuadoresArduinoLayout = new javax.swing.GroupLayout(panelActuadoresArduino);
        panelActuadoresArduino.setLayout(panelActuadoresArduinoLayout);
        panelActuadoresArduinoLayout.setHorizontalGroup(
            panelActuadoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActuadoresArduinoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnVentilador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLED, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnServo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelActuadoresArduinoLayout.setVerticalGroup(
            panelActuadoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActuadoresArduinoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelActuadoresArduinoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVentilador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLED, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnServo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelAlertas.setBackground(new java.awt.Color(255, 250, 240));
        panelAlertas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "🚨 Alertas Basadas en Código Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(200, 100, 50)));

        lblAlertas.setFont(new java.awt.Font("Arial", 0, 12));
        lblAlertas.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblAlertas.setText("<html><b>🚨 ALERTAS BASADAS EN CÓDIGO ARDUINO:</b><br>Inicializando sistema...</html>");

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
                .addComponent(lblAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelUmbralesInfo.setBackground(new java.awt.Color(250, 255, 250));
        panelUmbralesInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "📊 Umbrales EXACTOS del Código Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 150, 50)));

        lblInfoUmbrales.setFont(new java.awt.Font("Arial", 0, 12));
        lblInfoUmbrales.setForeground(new java.awt.Color(60, 60, 60));
        lblInfoUmbrales.setText("<html><b>🔧 UMBRALES PROGRAMADOS EN TU ARDUINO:</b><br>🌡️ <b>Temperatura (LM35):</b> ≤50°C (Normal) | 51-55°C (Ventilador ON) | >56°C (Ventilador + Buzzer)<br>💧 <b>Humedad Suelo:</b> <500 (Válvula OFF) | ≥500 (Válvula ON)<br>☀️ <b>Luminosidad (LDR):</b> <500 (Día - LED OFF) | ≥500 (Noche - LED ON)<br>🚪 <b>Servo:</b> Controlado por botón (Pin 6) - Alterna entre 0° y 90°</html>");

        javax.swing.GroupLayout panelUmbralesInfoLayout = new javax.swing.GroupLayout(panelUmbralesInfo);
        panelUmbralesInfo.setLayout(panelUmbralesInfoLayout);
        panelUmbralesInfoLayout.setHorizontalGroup(
            panelUmbralesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoUmbrales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelUmbralesInfoLayout.setVerticalGroup(
            panelUmbralesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfoUmbrales, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelNavegacion.setBackground(new java.awt.Color(250, 250, 250));
        panelNavegacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnInicio.setBackground(new java.awt.Color(150, 200, 255));
        btnInicio.setFont(new java.awt.Font("Arial", 1, 14));
        btnInicio.setText("🏠 Inicio");
        btnInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioActionPerformed(evt);
            }
        });

        btnControlAutomatico.setBackground(new java.awt.Color(100, 255, 150));
        btnControlAutomatico.setFont(new java.awt.Font("Arial", 1, 14));
        btnControlAutomatico.setText("🤖 Control Automático");
        btnControlAutomatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlAutomaticoActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(255, 150, 150));
        btnSalir.setFont(new java.awt.Font("Arial", 1, 14));
        btnSalir.setText("🚪 Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        lblFechaHora.setFont(new java.awt.Font("Arial", 1, 12));
        lblFechaHora.setForeground(new java.awt.Color(100, 100, 100));
        lblFechaHora.setText("📅 --/--/---- --:--:--");

        javax.swing.GroupLayout panelNavegacionLayout = new javax.swing.GroupLayout(panelNavegacion);
        panelNavegacion.setLayout(panelNavegacionLayout);
        panelNavegacionLayout.setHorizontalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNavegacionLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lblFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnControlAutomatico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        panelNavegacionLayout.setVerticalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavegacionLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnControlAutomatico)
                    .addComponent(btnSalir)
                    .addComponent(lblFechaHora)
                    .addComponent(btnInicio))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSensoresArduino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelActuadoresArduino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNavegacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrincipalLayout.createSequentialGroup()
                        .addComponent(panelAlertas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelUmbralesInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelSensoresArduino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelActuadoresArduino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelUmbralesInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(panelNavegacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnVentiladorActionPerformed(java.awt.event.ActionEvent evt) {                                              
        ventiladorActivo = !ventiladorActivo;
        updateDisplays();
        String comando = ventiladorActivo ? "HIGH" : "LOW";
        JOptionPane.showMessageDialog(this, 
            "🌀 Ventilador " + (ventiladorActivo ? "ACTIVADO" : "DESACTIVADO") + "\n\n" +
            "📡 Comando Arduino:\n" +
            "pinMode(8, OUTPUT);\n" +
            "digitalWrite(8, " + comando + ");\n\n" +
            "🔧 En tu código Arduino, el ventilador se activa automáticamente\n" +
            "cuando la temperatura supera los 51°C.", 
            "Control de Ventilador", JOptionPane.INFORMATION_MESSAGE);
    }                                             

    private void btnBuzzerActionPerformed(java.awt.event.ActionEvent evt) {                                          
        buzzerActivo = !buzzerActivo;
        updateDisplays();
        String comando = buzzerActivo ? "HIGH" : "LOW";
        JOptionPane.showMessageDialog(this, 
            "🚨 Buzzer " + (buzzerActivo ? "ACTIVADO" : "DESACTIVADO") + "\n\n" +
            "📡 Comando Arduino:\n" +
            "pinMode(13, OUTPUT);\n" +
            "digitalWrite(13, " + comando + ");\n\n" +
            "🔧 En tu código Arduino, el buzzer se activa automáticamente\n" +
            "cuando la temperatura supera los 56°C.", 
            "Control de Buzzer", JOptionPane.INFORMATION_MESSAGE);
    }                                         

    private void btnLEDActionPerformed(java.awt.event.ActionEvent evt) {                                       
        ledActivo = !ledActivo;
        updateDisplays();
        String comando = ledActivo ? "HIGH" : "LOW";
        JOptionPane.showMessageDialog(this, 
            "💡 LED " + (ledActivo ? "ENCENDIDO" : "APAGADO") + "\n\n" +
            "📡 Comando Arduino:\n" +
            "pinMode(11, OUTPUT);\n" +
            "digitalWrite(11, " + comando + ");\n\n" +
            "🔧 En tu código Arduino, el LED se activa automáticamente\n" +
            "cuando el LDR detecta valores ≥500 (noche).", 
            "Control de LED", JOptionPane.INFORMATION_MESSAGE);
    }                                      

    private void btnValvulaActionPerformed(java.awt.event.ActionEvent evt) {                                           
        valvulaActiva = !valvulaActiva;
        updateDisplays();
        String comando = valvulaActiva ? "HIGH" : "LOW";
        JOptionPane.showMessageDialog(this, 
            "💧 Válvula de Riego " + (valvulaActiva ? "ABIERTA" : "CERRADA") + "\n\n" +
            "📡 Comando Arduino:\n" +
            "pinMode(12, OUTPUT);\n" +
            "digitalWrite(12, " + comando + ");\n\n" +
            "🔧 En tu código Arduino, la válvula se activa automáticamente\n" +
            "cuando la humedad del suelo es ≥500 (suelo húmedo).", 
            "Control de Válvula", JOptionPane.INFORMATION_MESSAGE);
    }                                          

    private void btnServoActionPerformed(java.awt.event.ActionEvent evt) {                                         
        servoAbierto = !servoAbierto;
        updateDisplays();
        int angulo = servoAbierto ? 90 : 0;
        JOptionPane.showMessageDialog(this, 
            "🚪 Servo movido a " + angulo + "°\n\n" +
            "📡 Comando Arduino:\n" +
            "#include <Servo.h>\n" +
            "Servo miServo;\n" +
            "miServo.attach(7);\n" +
            "miServo.write(" + angulo + ");\n\n" +
            "🔧 En tu código Arduino, el servo se controla con el botón\n" +
            "conectado al pin 6, alternando entre 0° y 90°.", 
            "Control de Servo", JOptionPane.INFORMATION_MESSAGE);
    }                                        

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (updateTimer != null) {
            updateTimer.stop();
        }
        new PanelPrincipal().setVisible(true);
        this.dispose();
    }                                         

    private void btnControlAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        if (updateTimer != null) {
            updateTimer.stop();
        }
        new ControlAutomatico().setVisible(true);
        this.dispose();
    }                                                     

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea salir?\n\n" +
            "Esto cerrará la interfaz de control manual del Arduino.", 
            "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (updateTimer != null) {
                updateTimer.stop();
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
            java.util.logging.Logger.getLogger(ControlManual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlManual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlManual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlManual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlManual().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnBuzzer;
    private javax.swing.JButton btnControlAutomatico;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnLED;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnServo;
    private javax.swing.JButton btnValvula;
    private javax.swing.JButton btnVentilador;
    private javax.swing.JLabel lblAlertas;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblHumedadSuelo;
    private javax.swing.JLabel lblInfoUmbrales;
    private javax.swing.JLabel lblLuminosidad;
    private javax.swing.JLabel lblTemperaturaLM35;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelActuadoresArduino;
    private javax.swing.JPanel panelAlertas;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelSensoresArduino;
    private javax.swing.JPanel panelUmbralesInfo;
    private javax.swing.JProgressBar progressHumedad;
    private javax.swing.JProgressBar progressLuz;
    private javax.swing.JProgressBar progressTemperatura;
    // End of variables declaration                   
}