⚡ Check out my (german) remote talk about embedded development. We will start with a quick kick-start and get into debugging practices. Sign up [here](https://www.eventbrite.de/e/embedded-entwicklung-ist-hart-aber-wir-sind-harter-tickets-99012865145?aff=mc)
# ITM Viewer

ITM Viewer allows you to view the messages sent through the ARM ITM data channels.

*The plugin is at the start of the development and some things may not work as stable as planned, so please use it with patience :)*

![ITM Viewer Plugin Demo](./doc/demo.gif)

## Getting Started
This plugin parses the TCL RPC Server output of openocd. In order to activate it you must activate the tcl server.
### Prerequisites
* openocd
* CLion

Example config for STM32F411 "blackpill": \
***Note:*** Set your CPU clock value in `tpiu config internal - uart off 100000000` otherwise it won't work

```
# OpenOCD configuration for the ("blackpill") STM32F411CUX development board
# External v2.0 stlink
source [find interface/stlink.cfg]

transport select hla_swd
set WORKAREASIZE 0x40000

source [find target/stm32f4x.cfg]

# activate tcl server
itm ports on
tcl_port 6666
# for configuration please see §16.6.3 http://openocd.org/doc/html/Architecture-and-Core-Commands.html
tpiu config internal - uart off 100000000
# activate ports (configurable)
itm port 24 on
itm port 25 on
itm port 26 on
itm port 27 on

reset_config none

init
arm semihosting enable
```
And for the corresponding configuration in the CLion plugin config:
![ITM Viewer Plugin Configuration](./doc/itm_viewer_settings.png)

### Example 
A minimal C example to send message via ITM (for STM32F746):
```c
#define LOG_DEBUG_LEVEL 24
#define LOG_INFO_LEVEL 25
#define LOG_WARN_LEVEL 26
#define LOG_ERROR_LEVEL 27

#define LOG_DEBUG(msg) println(msg, LOG_DEBUG_LEVEL)
#define LOG_INFO(msg) println(msg, LOG_INFO_LEVEL)
#define LOG_WARN(msg) println(msg, LOG_WARN_LEVEL)
#define LOG_ERROR(msg) println(msg, LOG_ERROR_LEVEL)

void print(char _char, uint8_t level) {
    while (ITM->PORT[level].u32 == 0UL) {
        __NOP();
    }
    ITM->PORT[level].u8 = (uint8_t) _char;
}

void println(const char* msg, uint8_t level) {
    if(msg == NULL || level > 31) {
        return;
    }
    for(int i = 0; i<strlen(msg); i++) {
        print(msg[i], level);
    }
    print('\n', level);
}

void itm_test(void *pvParameters) {
    while(1) {
        LOG_INFO("Hello World!");
        vTaskDelay(1000); // or HAL_Delay
    }
}
```

### Installing
#### JetBrains Plugin Repository
ITM Viewer is available in the JetBrains Plugin Repository https://plugins.jetbrains.com/plugin/14163-itm-viewer.

#### Manual
The plugin uses the jetbrains gradle plugin template. In order to build it you have to pull the dependencies and build the release version:

`gradle buildPlugin`

After project build:
- Copy from `<Project root>/build/libs/itm_viewer-1.1.0.jar`
- To `<Clion instalation folder>/plugins/itm-viewer` folder
- Then open IDE: Settings -> Plugins -> enable plugin
- ![Plugin](./doc/plugin.png)
  
#### Run configuration
![Run/Debug](./doc/run_config.png)

## Authors

* **Mohamad Ramadan** - [ramdadam](https://github.com/ramdadam)

Thanks for the awesome work with [cmake-conan](https://github.com/conan-io/cmake-conan) that helped me get started.
 
Also big thanks to my colleagues at cosee for helping me out with the Java stuff :) 

## License

This project is licensed under the MIT License - see the [License](License) file for details
