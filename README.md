# 🌱 SISTEMA DE INVERNADERO INTELIGENTE
## Manual de Usuario - Interfaz de Control Java

---

### **INFORMACIÓN DEL PROYECTO**
- **Proyecto:** Sistema de Monitoreo y Control de Invernadero Inteligente
- **Plataforma:** Java NetBeans + Arduino
- **Autor:** Nicom
- **Versión:** 1.0
- **Fecha:** 2025

---

## 📋 **ÍNDICE**
1. [Introducción](#introduccion)
2. [Requisitos del Sistema](#requisitos)
3. [Instalación y Configuración](#instalacion)
4. [Navegación de la Interfaz](#navegacion)
5. [Pantallas del Sistema](#pantallas)
6. [Control Manual](#control-manual)
7. [Control Automático](#control-automatico)
8. [Especificaciones Técnicas](#especificaciones)
9. [Solución de Problemas](#troubleshooting)

---

## 🎯 **1. INTRODUCCIÓN** {#introduccion}

El **Sistema de Invernadero Inteligente** es una aplicación Java que simula y controla un invernadero automatizado conectado a Arduino. La interfaz permite monitorear sensores en tiempo real y controlar actuadores tanto manual como automáticamente.

### **Características Principales:**
- 📡 Monitoreo de sensores Arduino (LM35, Humedad, LDR)
- 🎛️ Control manual de actuadores
- 🤖 Sistema automático inteligente basado en umbrales
- 🖥️ Interfaz gráfica intuitiva y profesional
- 📊 Registro de actividades en tiempo real

---

## 💻 **2. REQUISITOS DEL SISTEMA** {#requisitos}

### **Software Necesario:**
- ✅ Java JDK 8 o superior
- ✅ NetBeans IDE (recomendado)
- ✅ Arduino IDE (para hardware real)

### **Hardware Compatible:**
- ✅ Arduino Uno/Nano con sensores:
  - 🌡️ Sensor de temperatura LM35 (Pin A0)
  - 💧 Sensor de humedad del suelo (Pin A1)
  - ☀️ Sensor LDR de luminosidad (Pin A2)
- ✅ Actuadores:
  - 🌀 Ventilador (Pin 8)
  - 💡 LED (Pin 11)
  - 💧 Válvula de riego (Pin 12)
  - 🚨 Buzzer (Pin 13)
  - 🚪 Servo motor (Pin 7)

### **Umbrales Configurados en Arduino:**
- **Temperatura:** ≤50°C (Normal) | 51-55°C (Ventilador) | >56°C (Buzzer + Ventilador)
- **Humedad:** <500 (Seco) | ≥500 (Activar riego)
- **Luminosidad:** <500 (Día) | ≥500 (Noche - Activar LED)

---

## 🚀 **3. INSTALACIÓN Y CONFIGURACIÓN** {#instalacion}

### **Paso 1: Configurar el Proyecto**
1. Abrir NetBeans IDE
2. Crear nuevo proyecto Java
3. Importar los 4 archivos principales:
   - `PantallaBienvenida.java`
   - `PanelPrincipal.java`
   - `ControlManual.java`
   - `ControlAutomatico.java`

### **Paso 2: Configurar Main Class**
1. Clic derecho en el proyecto → **Properties**
2. Ir a **Run** → **Main Class**
3. Establecer: `ui_invernadero.PantallaBienvenida`

### **Paso 3: Compilar y Ejecutar**
1. **Build** → **Clean and Build Project**
2. **Run** → **Run Project**
3. El sistema iniciará automáticamente con la pantalla de bienvenida

---

## 🧭 **4. NAVEGACIÓN DE LA INTERFAZ** {#navegacion}

### **Flujo de Pantallas:**
```
PantallaBienvenida (3 segundos) 
          ↓
    PanelPrincipal (Hub Central)
         ↙    ↘
ControlManual  ControlAutomatico
    ↓               ↓
   🏠 Inicio ←---→ 🏠 Inicio
```

### **Botones de Navegación:**
- 🏠 **Inicio:** Regresa al Panel Principal
- 🎛️ **Control Manual:** Abre la interfaz de control manual
- 🤖 **Control Automático:** Abre el sistema automático
- 🚪 **Salir:** Cierra la aplicación (con confirmación)

---

## 📱 **5. PANTALLAS DEL SISTEMA** {#pantallas}

### **5.1 Pantalla de Bienvenida**
- **Función:** Introducción y carga del sistema
- **Duración:** 3 segundos de carga automática
- **Información mostrada:**
  - Descripción del proyecto
  - Especificaciones del hardware Arduino
  - Barra de progreso de inicialización

### **5.2 Panel Principal (Hub Central)**
- **Función:** Centro de navegación y monitoreo general
- **Características:**
  - 📊 Datos de sensores en tiempo real
  - 🕒 Reloj digital
  - 🚨 Estado de actuadores
  - 📋 Información de umbrales Arduino
  - 🎛️ Botones de navegación a otras pantallas

### **5.3 Control Manual**
- **Función:** Control directo de todos los actuadores
- **Características:**
  - 📡 Monitoreo de sensores con barras de progreso
  - ⚙️ Control individual de cada actuador
  - 🚨 Alertas basadas en umbrales Arduino
  - 📋 Información técnica de comandos Arduino

### **5.4 Control Automático**
- **Función:** Sistema inteligente basado en umbrales
- **Características:**
  - ⚙️ Configuración personalizable de umbrales
  - 🤖 Control automático cada 3 segundos
  - 📝 Log detallado de actividades
  - 📊 Estado visual de todos los actuadores

---

## 🎛️ **6. CONTROL MANUAL** {#control-manual}

### **6.1 Sensores Monitoreados**
- **🌡️ LM35 (Pin A0):** Temperatura en °C (rango 15-70°C)
- **💧 Humedad (Pin A1):** Porcentaje y valor analógico (0-1023)
- **☀️ LDR (Pin A2):** Estado día/noche y valor analógico

### **6.2 Actuadores Controlables**
| Actuador | Pin Arduino | Función | Estado Visual |
|----------|-------------|---------|---------------|
| 🌀 Ventilador | Pin 8 | Refrigeración | Verde (ON) / Gris (OFF) |
| 🚨 Buzzer | Pin 13 | Alarma sonora | Rojo (ON) / Gris (OFF) |
| 💡 LED | Pin 11 | Iluminación | Amarillo (ON) / Gris (OFF) |
| 💧 Válvula | Pin 12 | Sistema de riego | Azul (ON) / Gris (OFF) |
| 🚪 Servo | Pin 7 | Ventilación extra | Cyan (90°) / Gris (0°) |

### **6.3 Uso del Control Manual**
1. **Activar actuadores:** Clic en el botón del actuador deseado
2. **Ver información técnica:** Cada clic muestra el comando Arduino equivalente
3. **Monitorear alertas:** Panel de alertas muestra recomendaciones automáticas
4. **Observar umbrales:** Panel inferior muestra los límites programados en Arduino

---

## 🤖 **7. CONTROL AUTOMÁTICO** {#control-automatico}

### **7.1 Configuración de Umbrales**
El sistema permite personalizar los umbrales de activación:

- **🌡️ Temperatura Normal:** Valor máximo sin activar actuadores
- **🌀 Temperatura Ventilador:** Valor para activar ventilador
- **🚨 Temperatura Buzzer:** Valor para activar buzzer + ventilador
- **💧 Umbral Humedad:** Valor analógico para activar riego
- **☀️ Umbral LDR:** Valor para detectar noche y activar LED

### **7.2 Lógica Automática**
El sistema replica exactamente la lógica del Arduino:

```
SI temperatura > 56°C:
    Activar ventilador + buzzer
SI temperatura > 51°C:
    Activar solo ventilador
SI temperatura ≤ 50°C:
    Desactivar ventilador y buzzer

SI humedad ≥ 500:
    Activar válvula de riego
SI humedad < 500:
    Desactivar válvula

SI LDR ≥ 500:
    Activar LED (noche)
SI LDR < 500:
    Desactivar LED (día)
```

### **7.3 Uso del Control Automático**
1. **Configurar umbrales:** Ajustar valores en los spinners
2. **Aplicar configuración:** Clic en "Aplicar Configuración"
3. **Iniciar automático:** Clic en "🚀 Iniciar Automático"
4. **Monitorear actividad:** Observar el log de actividades
5. **Detener si necesario:** Clic en "⏹️ Detener Automático"

### **7.4 Log de Actividades**
- Registro cronológico de todas las acciones automáticas
- Timestamps precisos
- Información detallada de valores de sensores
- Razón de cada activación/desactivación

---

## ⚙️ **8. ESPECIFICACIONES TÉCNICAS** {#especificaciones}

### **8.1 Arquitectura del Software**
- **Lenguaje:** Java
- **Framework GUI:** Swing
- **Patrón de diseño:** MVC (Model-View-Controller)
- **Timers:** javax.swing.Timer para actualizaciones

### **8.2 Configuración de Hardware Arduino**
```arduino
// Pines analógicos (sensores)
const int pinSensorTemp = A0;   // LM35
const int sensorHumedad = A1;   // Sensor humedad suelo
const int analogPinLDR = A2;    // LDR

// Pines digitales (actuadores)
const int pinVentilador = 8;    // Ventilador
const int pinBuzzer = 13;       // Buzzer
const int ledPin = 11;          // LED
const int valvula = 12;         // Válvula riego
const int pinServo = 7;         // Servo motor
const int pinBoton = 6;         // Botón control servo
```

### **8.3 Umbrales por Defecto**
- **Temperatura:** 50°C (normal), 51°C (ventilador), 56°C (buzzer)
- **Humedad:** 500 (analógico)
- **Luminosidad:** 500 (analógico)

### **8.4 Frecuencias de Actualización**
- **Sensores:** Cada 1 segundo
- **Control automático:** Cada 3 segundos
- **Interfaz:** Tiempo real

---

## 🔧 **9. SOLUCIÓN DE PROBLEMAS** {#troubleshooting}

### **9.1 Problemas Comunes**

#### **Error de compilación**
- **Problema:** "Cannot find symbol"
- **Solución:** Verificar que todos los archivos estén en el paquete `ui_invernadero`

#### **Interfaz no responde**
- **Problema:** Botones no funcionan
- **Solución:** Verificar que los métodos de eventos estén correctamente conectados

#### **Valores de sensores irreales**
- **Problema:** Simulación con valores extraños
- **Solución:** Los valores se ajustan automáticamente a rangos realistas

### **9.2 Configuración Recomendada**
- **Resolución mínima:** 1024x768
- **Look and Feel:** Nimbus (configurado automáticamente)
- **JVM Memory:** 512MB mínimo

### **9.3 Mantenimiento**
- **Limpiar log:** Usar botón "🗑️ Limpiar Log" en control automático
- **Reiniciar sistema:** Salir y volver a ejecutar
- **Actualizar umbrales:** Modificar valores en control automático

---

## 📞 **SOPORTE Y CONTACTO**

Para soporte técnico o consultas sobre el sistema:
- **Desarrollador:** Nicom
- **Proyecto:** Sistema de Invernadero Inteligente
- **Tecnologías:** Java NetBeans + Arduino
- **Documentación:** Este manual de usuario

---

## 📄 **APÉNDICES**

### **Apéndice A: Comandos Arduino Equivalentes**
- `digitalWrite(8, HIGH/LOW)` - Control ventilador
- `digitalWrite(13, HIGH/LOW)` - Control buzzer  
- `digitalWrite(11, HIGH/LOW)` - Control LED
- `digitalWrite(12, HIGH/LOW)` - Control válvula
- `miServo.write(0/90)` - Control servo

### **Apéndice B: Rangos de Sensores**
- **LM35:** 0°C a 100°C (precisión ±0.5°C)
- **Humedad:** 0-1023 (analógico)
- **LDR:** 0-1023 (analógico)

### **Apéndice C: Códigos de Estado**
- 🟢 Verde: Actuador activo
- 🔴 Rojo: Alerta crítica
- 🟡 Amarillo: Advertencia
- ⚪ Gris: Actuador inactivo

---

**© 2025 - Sistema de Invernadero Inteligente - Todos los derechos reservados**
