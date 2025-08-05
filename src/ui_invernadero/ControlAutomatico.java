package ui_invernadero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Control Automático - Sistema basado en umbrales de Arduino
 * Sensores: LM35(A0), Humedad(A1), LDR(A2)
 * Actuadores: Ventilador(8), LED(11), Válvula(12), Servo(7), Buzzer(13)
 * @author Nicom
 */
public class ControlAutomatico extends javax.swing.JFrame {
    
    private Timer updateTimer;
    private Timer automaticTimer;
    private boolean modoAutomaticoActivo = false;
    
    // Variables del Arduino - Valores reales simulados
    private double temperaturaLM35 = 25.0;        // Sensor LM35 (Pin A0)
    private int humedadAnalogica = 450;           // Sensor humedad (Pin A1) 0-1023
    private int valorLDR = 300;                   // Sensor LDR (Pin A2) 0-1023
    
    // Umbrales configurables (valores por defecto del Arduino)
    private double tempNormal = 50.0;             // ≤50°C normal
    private double tempVentilador = 51.0;         // 51-55°C ventilador
    private double tempBuzzer = 56.0;             // >56°C buzzer
    private int umbralHumedad = 500;              // ≥500 riego
    private int umbralLuz = 500;                  // ≥500 noche
    
    // Estados de actuadores automáticos
    private boolean ventiladorAutoActivo = false;
    private boolean buzzerAutoActivo = false;
    private boolean ledAutoActivo = false;
    private boolean valvulaAutoActiva = false;
    private boolean servoAutoAbierto = false;
    
    public ControlAutomatico() {
        initComponents();
        setupCustomComponents();
        startUpdateTimer();
    }
    
    private void setupCustomComponents() {
        // Configurar ventana
        setTitle("🤖 Control Automático - Arduino Invernadero Inteligente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar fondo
        getContentPane().setBackground(new Color(240, 255, 240));
        
        // Configurar valores iniciales de los spinners (umbrales del Arduino)
        spinnerTempNormal.setValue(tempNormal);
        spinnerTempVentilador.setValue(tempVentilador);
        spinnerTempBuzzer.setValue(tempBuzzer);
        spinnerUmbralHumedad.setValue(umbralHumedad);
        spinnerUmbralLuz.setValue(umbralLuz);
        
        // Actualizar displays iniciales
        updateDisplays();
        
        // Timer para actualizar información cada segundo
        updateTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDateTime();
                simularSensoresArduino();
                if (modoAutomaticoActivo) {
                    verificarCondicionesAutomaticas();
                }
            }
        });
        
        // Timer para control automático (cada 3 segundos)
        automaticTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modoAutomaticoActivo) {
                    ejecutarControlAutomatico();
                }
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
        temperaturaLM35 += (Math.random() - 0.5) * 2.0;
        humedadAnalogica += (int)((Math.random() - 0.5) * 50);
        valorLDR += (int)((Math.random() - 0.5) * 40);
        
        // Mantener rangos realistas del Arduino
        if (temperaturaLM35 < 15) temperaturaLM35 = 15;
        if (temperaturaLM35 > 75) temperaturaLM35 = 75;
        if (humedadAnalogica < 200) humedadAnalogica = 200;
        if (humedadAnalogica > 800) humedadAnalogica = 800;
        if (valorLDR < 100) valorLDR = 100;
        if (valorLDR > 900) valorLDR = 900;
        
        updateDisplays();
    }
    
    private void verificarCondicionesAutomaticas() {
        // Obtener umbrales actuales de los spinners
        tempNormal = (Double) spinnerTempNormal.getValue();
        tempVentilador = (Double) spinnerTempVentilador.getValue();
        tempBuzzer = (Double) spinnerTempBuzzer.getValue();
        umbralHumedad = (Integer) spinnerUmbralHumedad.getValue();
        umbralLuz = (Integer) spinnerUmbralLuz.getValue();
        
        // Verificar condiciones y actualizar alertas
        updateAlertas();
    }
    
    private void ejecutarControlAutomatico() {
        if (!modoAutomaticoActivo) return;
        
        boolean accionTomada = false;
        
        // Control de temperatura (exacto como en Arduino)
        if (temperaturaLM35 > tempBuzzer) {
            // >56°C: Ventilador + Buzzer
            if (!ventiladorAutoActivo) {
                ventiladorAutoActivo = true;
                accionTomada = true;
                addLogEntry("🌀 Ventilador activado automáticamente (Temp: " + String.format("%.1f", temperaturaLM35) + "°C > " + tempBuzzer + "°C)");
            }
            if (!buzzerAutoActivo) {
                buzzerAutoActivo = true;
                accionTomada = true;
                addLogEntry("🚨 Buzzer activado automáticamente (Temp: " + String.format("%.1f", temperaturaLM35) + "°C > " + tempBuzzer + "°C)");
            }
        } else if (temperaturaLM35 > tempVentilador) {
            // 51-55°C: Solo ventilador
            if (!ventiladorAutoActivo) {
                ventiladorAutoActivo = true;
                accionTomada = true;
                addLogEntry("🌀 Ventilador activado automáticamente (Temp: " + String.format("%.1f", temperaturaLM35) + "°C)");
            }
            if (buzzerAutoActivo) {
                buzzerAutoActivo = false;
                accionTomada = true;
                addLogEntry("🚨 Buzzer desactivado automáticamente");
            }
        } else {
            // ≤50°C: Todo OFF
            if (ventiladorAutoActivo) {
                ventiladorAutoActivo = false;
                accionTomada = true;
                addLogEntry("🌀 Ventilador desactivado automáticamente (Temp: " + String.format("%.1f", temperaturaLM35) + "°C ≤ " + tempNormal + "°C)");
            }
            if (buzzerAutoActivo) {
                buzzerAutoActivo = false;
                accionTomada = true;
                addLogEntry("🚨 Buzzer desactivado automáticamente");
            }
        }
        
        // Control de humedad (exacto como en Arduino)
        if (humedadAnalogica >= umbralHumedad) {
            // ≥500: Activar riego
            if (!valvulaAutoActiva) {
                valvulaAutoActiva = true;
                accionTomada = true;
                addLogEntry("💧 Válvula activada automáticamente (Humedad: " + humedadAnalogica + " ≥ " + umbralHumedad + ")");
            }
        } else {
            // <500: Desactivar riego
            if (valvulaAutoActiva) {
                valvulaAutoActiva = false;
                accionTomada = true;
                addLogEntry("💧 Válvula desactivada automáticamente (Humedad: " + humedadAnalogica + " < " + umbralHumedad + ")");
            }
        }
        
        // Control de luminosidad (exacto como en Arduino)
        if (valorLDR >= umbralLuz) {
            // ≥500: Noche - LED ON
            if (!ledAutoActivo) {
                ledAutoActivo = true;
                accionTomada = true;
                addLogEntry("💡 LED activado automáticamente (LDR: " + valorLDR + " ≥ " + umbralLuz + " - Noche detectada)");
            }
        } else {
            // <500: Día - LED OFF
            if (ledAutoActivo) {
                ledAutoActivo = false;
                accionTomada = true;
                addLogEntry("💡 LED desactivado automáticamente (LDR: " + valorLDR + " < " + umbralLuz + " - Día detectado)");
            }
        }
        
        // Control de servo (ventilación extra basada en temperatura alta)
        if (temperaturaLM35 > tempVentilador && !servoAutoAbierto) {
            servoAutoAbierto = true;
            accionTomada = true;
            addLogEntry("🚪 Servo abierto automáticamente para ventilación extra (90°)");
        } else if (temperaturaLM35 <= tempNormal && servoAutoAbierto) {
            servoAutoAbierto = false;
            accionTomada = true;
            addLogEntry("🚪 Servo cerrado automáticamente (0°)");
        }
        
        if (accionTomada) {
            updateDisplays();
        }
    }
    
    private void updateAlertas() {
        String alertas = "<html><b>🚨 ESTADO SEGÚN UMBRALES ARDUINO:</b><br>";
        
        // Alertas de temperatura
        if (temperaturaLM35 > tempBuzzer) {
            alertas += "<font color='red'>🔥 CRÍTICO: Temp > " + tempBuzzer + "°C - Ventilador + Buzzer</font><br>";
        } else if (temperaturaLM35 > tempVentilador) {
            alertas += "<font color='orange'>🌡️ ALTO: Temp > " + tempVentilador + "°C - Solo Ventilador</font><br>";
        } else {
            alertas += "<font color='green'>✅ NORMAL: Temp ≤ " + tempNormal + "°C</font><br>";
        }
        
        // Alertas de humedad
        if (humedadAnalogica >= umbralHumedad) {
            alertas += "<font color='blue'>💧 HÚMEDO: " + humedadAnalogica + " ≥ " + umbralHumedad + " - Riego activo</font><br>";
        } else {
            alertas += "<font color='brown'>🏜️ SECO: " + humedadAnalogica + " < " + umbralHumedad + " - Sin riego</font><br>";
        }
        
        // Alertas de luminosidad
        if (valorLDR >= umbralLuz) {
            alertas += "<font color='purple'>🌙 NOCHE: " + valorLDR + " ≥ " + umbralLuz + " - LED activo</font><br>";
        } else {
            alertas += "<font color='gold'>☀️ DÍA: " + valorLDR + " < " + umbralLuz + " - LED inactivo</font><br>";
        }
        
        alertas += "</html>";
        lblAlertas.setText(alertas);
    }
    
    private void addLogEntry(String mensaje) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String entrada = "[" + sdf.format(new Date()) + "] " + mensaje + "\n";
        txtLogAutomatico.append(entrada);
        txtLogAutomatico.setCaretPosition(txtLogAutomatico.getDocument().getLength());
    }
    
    private void updateDisplays() {
        // Actualizar sensores
        lblTemperaturaLM35.setText(String.format("🌡️ LM35: %.1f°C", temperaturaLM35));
        int humedadPorcentaje = (int)map(humedadAnalogica, 1023, 0, 0, 100);
        lblHumedadSuelo.setText(String.format("💧 Humedad: %d%% (%d)", humedadPorcentaje, humedadAnalogica));
        String estadoLuz = valorLDR >= umbralLuz ? "🌙 Noche" : "☀️ Día";
        lblLuminosidad.setText(String.format("☀️ LDR: %s (%d)", estadoLuz, valorLDR));
        
        // Actualizar barras de progreso
        progressTemperatura.setValue((int)temperaturaLM35);
        progressTemperatura.setString(String.format("%.1f°C", temperaturaLM35));
        progressHumedad.setValue(humedadPorcentaje);
        progressHumedad.setString(String.format("%d%% (%d)", humedadPorcentaje, humedadAnalogica));
        
        // Actualizar estado del modo automático
        lblEstadoAutomatico.setText("Modo Automático: " + (modoAutomaticoActivo ? "🟢 ACTIVO" : "🔴 INACTIVO"));
        btnIniciarAutomatico.setEnabled(!modoAutomaticoActivo);
        btnDetenerAutomatico.setEnabled(modoAutomaticoActivo);
        
        // Actualizar estado de actuadores
        lblEstadoVentilador.setText("🌀 Ventilador: " + (ventiladorAutoActivo ? "ON" : "OFF"));
        lblEstadoBuzzer.setText("🚨 Buzzer: " + (buzzerAutoActivo ? "ON" : "OFF"));
        lblEstadoLED.setText("💡 LED: " + (ledAutoActivo ? "ON" : "OFF"));
        lblEstadoValvula.setText("💧 Válvula: " + (valvulaAutoActiva ? "ON" : "OFF"));
        lblEstadoServo.setText("🚪 Servo: " + (servoAutoAbierto ? "90°" : "0°"));
        
        // Cambiar colores según estado
        lblEstadoVentilador.setForeground(ventiladorAutoActivo ? new Color(0, 150, 0) : Color.GRAY);
        lblEstadoBuzzer.setForeground(buzzerAutoActivo ? Color.RED : Color.GRAY);
        lblEstadoLED.setForeground(ledAutoActivo ? new Color(255, 150, 0) : Color.GRAY);
        lblEstadoValvula.setForeground(valvulaAutoActiva ? Color.BLUE : Color.GRAY);
        lblEstadoServo.setForeground(servoAutoAbierto ? Color.CYAN : Color.GRAY);
        
        // Actualizar alertas
        updateAlertas();
    }
    
    // Función map como en Arduino
    private double map(double value, double fromLow, double fromHigh, double toLow, double toHigh) {
        return (value - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        panelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelConfiguracion = new javax.swing.JPanel();
        panelUmbrales = new javax.swing.JPanel();
        lblTempNormal = new javax.swing.JLabel();
        spinnerTempNormal = new javax.swing.JSpinner();
        lblTempVentilador = new javax.swing.JLabel();
        spinnerTempVentilador = new javax.swing.JSpinner();
        lblTempBuzzer = new javax.swing.JLabel();
        spinnerTempBuzzer = new javax.swing.JSpinner();
        lblUmbralHumedad = new javax.swing.JLabel();
        spinnerUmbralHumedad = new javax.swing.JSpinner();
        lblUmbralLuz = new javax.swing.JLabel();
        spinnerUmbralLuz = new javax.swing.JSpinner();
        btnAplicarUmbrales = new javax.swing.JButton();
        panelSensoresAuto = new javax.swing.JPanel();
        lblTemperaturaLM35 = new javax.swing.JLabel();
        progressTemperatura = new javax.swing.JProgressBar();
        lblHumedadSuelo = new javax.swing.JLabel();
        progressHumedad = new javax.swing.JProgressBar();
        lblLuminosidad = new javax.swing.JLabel();
        lblAlertas = new javax.swing.JLabel();
        panelControlAuto = new javax.swing.JPanel();
        lblEstadoAutomatico = new javax.swing.JLabel();
        btnIniciarAutomatico = new javax.swing.JButton();
        btnDetenerAutomatico = new javax.swing.JButton();
        panelEstadoActuadores = new javax.swing.JPanel();
        lblEstadoVentilador = new javax.swing.JLabel();
        lblEstadoBuzzer = new javax.swing.JLabel();
        lblEstadoLED = new javax.swing.JLabel();
        lblEstadoValvula = new javax.swing.JLabel();
        lblEstadoServo = new javax.swing.JLabel();
        panelLog = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogAutomatico = new javax.swing.JTextArea();
        btnLimpiarLog = new javax.swing.JButton();
        panelNavegacion = new javax.swing.JPanel();
        btnInicio = new javax.swing.JButton();
        btnControlManual = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblFechaHora = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1200, 850));

        panelPrincipal.setBackground(new java.awt.Color(240, 255, 240));
        panelPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTitulo.setFont(new java.awt.Font("Arial", 1, 26));
        lblTitulo.setForeground(new java.awt.Color(50, 150, 50));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("🤖 CONTROL AUTOMÁTICO - ARDUINO INVERNADERO 🌱");

        panelConfiguracion.setBackground(new java.awt.Color(250, 255, 250));
        panelConfiguracion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "⚙️ Configuración de Umbrales Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 150, 50)));

        panelUmbrales.setBackground(new java.awt.Color(245, 255, 245));

        lblTempNormal.setFont(new java.awt.Font("Arial", 1, 12));
        lblTempNormal.setText("🌡️ Temp Normal (≤°C):");

        spinnerTempNormal.setFont(new java.awt.Font("Arial", 0, 12));
        spinnerTempNormal.setModel(new javax.swing.SpinnerNumberModel(50.0d, 30.0d, 70.0d, 1.0d));

        lblTempVentilador.setFont(new java.awt.Font("Arial", 1, 12));
        lblTempVentilador.setText("🌀 Temp Ventilador (>°C):");

        spinnerTempVentilador.setFont(new java.awt.Font("Arial", 0, 12));
        spinnerTempVentilador.setModel(new javax.swing.SpinnerNumberModel(51.0d, 35.0d, 75.0d, 1.0d));

        lblTempBuzzer.setFont(new java.awt.Font("Arial", 1, 12));
        lblTempBuzzer.setText("🚨 Temp Buzzer (>°C):");

        spinnerTempBuzzer.setFont(new java.awt.Font("Arial", 0, 12));
        spinnerTempBuzzer.setModel(new javax.swing.SpinnerNumberModel(56.0d, 40.0d, 80.0d, 1.0d));

        lblUmbralHumedad.setFont(new java.awt.Font("Arial", 1, 12));
        lblUmbralHumedad.setText("💧 Umbral Humedad (≥):");

        spinnerUmbralHumedad.setFont(new java.awt.Font("Arial", 0, 12));
        spinnerUmbralHumedad.setModel(new javax.swing.SpinnerNumberModel(500, 300, 800, 10));

        lblUmbralLuz.setFont(new java.awt.Font("Arial", 1, 12));
        lblUmbralLuz.setText("☀️ Umbral LDR (≥):");

        spinnerUmbralLuz.setFont(new java.awt.Font("Arial", 0, 12));
        spinnerUmbralLuz.setModel(new javax.swing.SpinnerNumberModel(500, 300, 800, 10));

        btnAplicarUmbrales.setBackground(new java.awt.Color(100, 200, 100));
        btnAplicarUmbrales.setFont(new java.awt.Font("Arial", 1, 12));
        btnAplicarUmbrales.setText("⚙️ Aplicar Configuración");
        btnAplicarUmbrales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarUmbralesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUmbralesLayout = new javax.swing.GroupLayout(panelUmbrales);
        panelUmbrales.setLayout(panelUmbralesLayout);
        panelUmbralesLayout.setHorizontalGroup(
            panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTempNormal)
                    .addComponent(lblUmbralHumedad))
                .addGap(18, 18, 18)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnerTempNormal, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(spinnerUmbralHumedad))
                .addGap(30, 30, 30)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTempVentilador)
                    .addComponent(lblUmbralLuz))
                .addGap(18, 18, 18)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnerTempVentilador, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(spinnerUmbralLuz))
                .addGap(30, 30, 30)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTempBuzzer)
                    .addComponent(btnAplicarUmbrales))
                .addGap(18, 18, 18)
                .addComponent(spinnerTempBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelUmbralesLayout.setVerticalGroup(
            panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUmbralesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTempNormal)
                    .addComponent(spinnerTempNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTempVentilador)
                    .addComponent(spinnerTempVentilador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTempBuzzer)
                    .addComponent(spinnerTempBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelUmbralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUmbralHumedad)
                    .addComponent(spinnerUmbralHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUmbralLuz)
                    .addComponent(spinnerUmbralLuz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarUmbrales))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelConfiguracionLayout = new javax.swing.GroupLayout(panelConfiguracion);
        panelConfiguracion.setLayout(panelConfiguracionLayout);
        panelConfiguracionLayout.setHorizontalGroup(
            panelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUmbrales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelConfiguracionLayout.setVerticalGroup(
            panelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelUmbrales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelSensoresAuto.setBackground(new java.awt.Color(240, 250, 255));
        panelSensoresAuto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "📡 Monitoreo y Alertas Arduino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 100, 200)));

        lblTemperaturaLM35.setFont(new java.awt.Font("Arial", 1, 16));
        lblTemperaturaLM35.setForeground(new java.awt.Color(200, 50, 50));
        lblTemperaturaLM35.setText("🌡️ LM35: 25.0°C");

        progressTemperatura.setMaximum(80);
        progressTemperatura.setValue(25);
        progressTemperatura.setStringPainted(true);
        progressTemperatura.setString("25.0°C");
        progressTemperatura.setForeground(new java.awt.Color(255, 100, 100));

        lblHumedadSuelo.setFont(new java.awt.Font("Arial", 1, 16));
        lblHumedadSuelo.setForeground(new java.awt.Color(50, 150, 200));
        lblHumedadSuelo.setText("💧 Humedad: 45% (450)");

        progressHumedad.setValue(45);
        progressHumedad.setStringPainted(true);
        progressHumedad.setString("45% (450)");
        progressHumedad.setForeground(new java.awt.Color(100, 150, 255));

        lblLuminosidad.setFont(new java.awt.Font("Arial", 1, 16));
        lblLuminosidad.setForeground(new java.awt.Color(255, 150, 0));
        lblLuminosidad.setText("☀️ LDR: Día (300)");

        lblAlertas.setFont(new java.awt.Font("Arial", 0, 12));
        lblAlertas.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblAlertas.setText("<html><b>🚨 ESTADO SEGÚN UMBRALES ARDUINO:</b><br>Inicializando...</html>");
        lblAlertas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelSensoresAutoLayout = new javax.swing.GroupLayout(panelSensoresAuto);
        panelSensoresAuto.setLayout(panelSensoresAutoLayout);
        panelSensoresAutoLayout.setHorizontalGroup(
            panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSensoresAutoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTemperaturaLM35, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHumedadSuelo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLuminosidad, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAlertas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        panelSensoresAutoLayout.setVerticalGroup(
            panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSensoresAutoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlertas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSensoresAutoLayout.createSequentialGroup()
                        .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemperaturaLM35)
                            .addComponent(lblHumedadSuelo)
                            .addComponent(lblLuminosidad))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelSensoresAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(progressHumedad, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelControlAuto.setBackground(new java.awt.Color(240, 255, 240));
        panelControlAuto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "🤖 Control Automático", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(50, 150, 50)));

        lblEstadoAutomatico.setFont(new java.awt.Font("Arial", 1, 16));
        lblEstadoAutomatico.setForeground(new java.awt.Color(100, 100, 100));
        lblEstadoAutomatico.setText("Modo Automático: 🔴 INACTIVO");

        btnIniciarAutomatico.setBackground(new java.awt.Color(100, 255, 100));
        btnIniciarAutomatico.setFont(new java.awt.Font("Arial", 1, 14));
        btnIniciarAutomatico.setText("🚀 Iniciar Automático");
        btnIniciarAutomatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarAutomaticoActionPerformed(evt);
            }
        });

        btnDetenerAutomatico.setBackground(new java.awt.Color(255, 100, 100));
        btnDetenerAutomatico.setFont(new java.awt.Font("Arial", 1, 14));
        btnDetenerAutomatico.setText("⏹️ Detener Automático");
        btnDetenerAutomatico.setEnabled(false);
        btnDetenerAutomatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetenerAutomaticoActionPerformed(evt);
            }
        });

        panelEstadoActuadores.setBackground(new java.awt.Color(250, 250, 250));
        panelEstadoActuadores.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado Actuadores Arduino"));

        lblEstadoVentilador.setFont(new java.awt.Font("Arial", 1, 12));
        lblEstadoVentilador.setText("🌀 Ventilador: OFF");

        lblEstadoBuzzer.setFont(new java.awt.Font("Arial", 1, 12));
        lblEstadoBuzzer.setText("🚨 Buzzer: OFF");

        lblEstadoLED.setFont(new java.awt.Font("Arial", 1, 12));
        lblEstadoLED.setText("💡 LED: OFF");

        lblEstadoValvula.setFont(new java.awt.Font("Arial", 1, 12));
        lblEstadoValvula.setText("💧 Válvula: OFF");

        lblEstadoServo.setFont(new java.awt.Font("Arial", 1, 12));
        lblEstadoServo.setText("🚪 Servo: 0°");

        javax.swing.GroupLayout panelEstadoActuadoresLayout = new javax.swing.GroupLayout(panelEstadoActuadores);
        panelEstadoActuadores.setLayout(panelEstadoActuadoresLayout);
        panelEstadoActuadoresLayout.setHorizontalGroup(
            panelEstadoActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstadoActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEstadoActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEstadoVentilador)
                    .addComponent(lblEstadoBuzzer)
                    .addComponent(lblEstadoLED)
                    .addComponent(lblEstadoValvula)
                    .addComponent(lblEstadoServo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEstadoActuadoresLayout.setVerticalGroup(
            panelEstadoActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEstadoActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEstadoVentilador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstadoBuzzer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstadoLED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstadoValvula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEstadoServo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelControlAutoLayout = new javax.swing.GroupLayout(panelControlAuto);
        panelControlAuto.setLayout(panelControlAutoLayout);
        panelControlAutoLayout.setHorizontalGroup(
            panelControlAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlAutoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelControlAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEstadoAutomatico, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelControlAutoLayout.createSequentialGroup()
                        .addComponent(btnIniciarAutomatico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDetenerAutomatico, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(panelEstadoActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelControlAutoLayout.setVerticalGroup(
            panelControlAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlAutoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelControlAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelEstadoActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelControlAutoLayout.createSequentialGroup()
                        .addComponent(lblEstadoAutomatico)
                        .addGap(18, 18, 18)
                        .addGroup(panelControlAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnIniciarAutomatico)
                            .addComponent(btnDetenerAutomatico))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        panelLog.setBackground(new java.awt.Color(255, 255, 240));
        panelLog.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "📝 Log de Actividades Automáticas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14), new java.awt.Color(150, 150, 50)));

        txtLogAutomatico.setEditable(false);
        txtLogAutomatico.setBackground(new java.awt.Color(250, 250, 250));
        txtLogAutomatico.setColumns(20);
        txtLogAutomatico.setFont(new java.awt.Font("Consolas", 0, 11));
        txtLogAutomatico.setRows(5);
        txtLogAutomatico.setText("=== LOG DE CONTROL AUTOMÁTICO ARDUINO ===\nSistema inicializado. Esperando activación del modo automático...\nUmbrales configurados según código Arduino.\n");
        jScrollPane1.setViewportView(txtLogAutomatico);

        btnLimpiarLog.setBackground(new java.awt.Color(255, 200, 100));
        btnLimpiarLog.setFont(new java.awt.Font("Arial", 1, 12));
        btnLimpiarLog.setText("🗑️ Limpiar Log");
        btnLimpiarLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLogLayout = new javax.swing.GroupLayout(panelLog);
        panelLog.setLayout(panelLogLayout);
        panelLogLayout.setHorizontalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiarLog)
                .addContainerGap())
        );
        panelLogLayout.setVerticalGroup(
            panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiarLog))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btnControlManual.setBackground(new java.awt.Color(100, 150, 255));
        btnControlManual.setFont(new java.awt.Font("Arial", 1, 14));
        btnControlManual.setText("🎛️ Control Manual");
        btnControlManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlManualActionPerformed(evt);
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
                .addComponent(btnControlManual, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        panelNavegacionLayout.setVerticalGroup(
            panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelNavegacionLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panelNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnControlManual)
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
                    .addComponent(panelConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSensoresAuto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelControlAuto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNavegacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelSensoresAuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelControlAuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelNavegacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
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

    private void btnAplicarUmbralesActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        // Obtener valores de los spinners
        tempNormal = (Double) spinnerTempNormal.getValue();
        tempVentilador = (Double) spinnerTempVentilador.getValue();
        tempBuzzer = (Double) spinnerTempBuzzer.getValue();
        umbralHumedad = (Integer) spinnerUmbralHumedad.getValue();
        umbralLuz = (Integer) spinnerUmbralLuz.getValue();
        
        // Validar rangos lógicos
        if (tempNormal >= tempVentilador) {
            JOptionPane.showMessageDialog(this, "La temperatura normal debe ser menor que la del ventilador", "Error de Configuración", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (tempVentilador >= tempBuzzer) {
            JOptionPane.showMessageDialog(this, "La temperatura del ventilador debe ser menor que la del buzzer", "Error de Configuración", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Aplicar configuración
        addLogEntry("⚙️ Umbrales actualizados: Temp(≤" + tempNormal + "°C|>" + tempVentilador + "°C|>" + tempBuzzer + "°C), Humedad(≥" + umbralHumedad + "), LDR(≥" + umbralLuz + ")");
        JOptionPane.showMessageDialog(this, "Configuración de umbrales aplicada correctamente", "Configuración", JOptionPane.INFORMATION_MESSAGE);
        updateDisplays();
    }                                                  

    private void btnIniciarAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        modoAutomaticoActivo = true;
        automaticTimer.start();
        addLogEntry("🚀 MODO AUTOMÁTICO INICIADO - Control basado en umbrales Arduino");
        updateDisplays();
        JOptionPane.showMessageDialog(this, 
            "Modo automático iniciado!\n\n" +
            "El sistema controlará automáticamente los actuadores\n" +
            "según los umbrales configurados y los sensores Arduino.", 
            "Modo Automático", JOptionPane.INFORMATION_MESSAGE);
    }                                                     

    private void btnDetenerAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        modoAutomaticoActivo = false;
        automaticTimer.stop();
        
        // Detener todos los actuadores
        ventiladorAutoActivo = false;
        buzzerAutoActivo = false;
        ledAutoActivo = false;
        valvulaAutoActiva = false;
        servoAutoAbierto = false;
        
        addLogEntry("⏹️ MODO AUTOMÁTICO DETENIDO - Todos los actuadores desactivados");
        updateDisplays();
        JOptionPane.showMessageDialog(this, 
            "Modo automático detenido.\n\nTodos los actuadores han sido desactivados.", 
            "Modo Automático", JOptionPane.WARNING_MESSAGE);
    }                                                      

    private void btnLimpiarLogActionPerformed(java.awt.event.ActionEvent evt) {                                              
        txtLogAutomatico.setText("=== LOG DE CONTROL AUTOMÁTICO ARDUINO ===\nLog limpiado por el usuario.\n");
    }                                             

    private void btnInicioActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (updateTimer != null) updateTimer.stop();
        if (automaticTimer != null) automaticTimer.stop();
        new PanelPrincipal().setVisible(true);
        this.dispose();
    }                                         

    private void btnControlManualActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        if (updateTimer != null) updateTimer.stop();
        if (automaticTimer != null) automaticTimer.stop();
        new ControlManual().setVisible(true);
        this.dispose();
    }                                                 

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea salir?\n\n" +
            "Esto detendrá el control automático del Arduino.", 
            "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (updateTimer != null) updateTimer.stop();
            if (automaticTimer != null) automaticTimer.stop();
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
            java.util.logging.Logger.getLogger(ControlAutomatico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlAutomatico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlAutomatico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlAutomatico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlAutomatico().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAplicarUmbrales;
    private javax.swing.JButton btnControlManual;
    private javax.swing.JButton btnDetenerAutomatico;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnIniciarAutomatico;
    private javax.swing.JButton btnLimpiarLog;
    private javax.swing.JButton btnSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlertas;
    private javax.swing.JLabel lblEstadoBuzzer;
    private javax.swing.JLabel lblEstadoAutomatico;
    private javax.swing.JLabel lblEstadoLED;
    private javax.swing.JLabel lblEstadoServo;
    private javax.swing.JLabel lblEstadoValvula;
    private javax.swing.JLabel lblEstadoVentilador;
    private javax.swing.JLabel lblFechaHora;
    private javax.swing.JLabel lblHumedadSuelo;
    private javax.swing.JLabel lblLuminosidad;
    private javax.swing.JLabel lblTempBuzzer;
    private javax.swing.JLabel lblTempNormal;
    private javax.swing.JLabel lblTempVentilador;
    private javax.swing.JLabel lblTemperaturaLM35;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUmbralHumedad;
    private javax.swing.JLabel lblUmbralLuz;
    private javax.swing.JPanel panelConfiguracion;
    private javax.swing.JPanel panelControlAuto;
    private javax.swing.JPanel panelEstadoActuadores;
    private javax.swing.JPanel panelLog;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelSensoresAuto;
    private javax.swing.JPanel panelUmbrales;
    private javax.swing.JProgressBar progressHumedad;
    private javax.swing.JProgressBar progressTemperatura;
    private javax.swing.JSpinner spinnerTempBuzzer;
    private javax.swing.JSpinner spinnerTempNormal;
    private javax.swing.JSpinner spinnerTempVentilador;
    private javax.swing.JSpinner spinnerUmbralHumedad;
    private javax.swing.JSpinner spinnerUmbralLuz;
    private javax.swing.JTextArea txtLogAutomatico;
    // End of variables declaration                   
}