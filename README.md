# NoPlayerStop

NoPlayerStop is a lightweight utility that enables your server to operate on demand. By automatically shutting down when no players are active, it transforms your setup into a **serverless-like environment**, saving CPU and RAM resources during downtime.

## Supported Platforms

* **Fabric**
* **NeoForge**
* **Paper / Spigot / Purpur**
* **Velocity**

-----

## Serverless Infrastructure with MC-Gate

This mod is designed to work as the perfect companion for [mc-gate](https://github.com/SirCesarium/mc-gate).

While **NoPlayerStop** handles the automated shutdown when your server is empty, **mc-gate** acts as a high-performance async proxy that stays online. When a player attempts to join or pings the server, **mc-gate** detects the traffic and executes a command to wake the server back up.

Together, they create a fully automated **On-Demand** ecosystem: your server only consumes hardware resources when someone is actually playing.

-----

## Key Features

* **Smart Idle Detection**: Monitors player activity and triggers a shutdown sequence only when the server is truly empty.
* **Customizable Grace Period**: Configurable delays to ensure the server doesn't shut down during quick relogs.
* **Live Countdown**: Provides real-time warnings in the console before the final shutdown occurs.
* **Webhook Integration**: Send automated notifications to **Discord** or custom web services the moment the server goes offline.
* **Native Performance**: Built to be as lightweight as possible with zero impact on tick rates or game performance.

## Configuration

The configuration file is generated automatically on the first run. You can adjust the following parameters:

* `enabled`: Toggle the entire system on or off.
* `shutdown-delay`: How many seconds to wait after the last player leaves.
* `warning-seconds`: When to start broadcasting shutdown warnings.
* `min-players`: The player count threshold to start the timer (usually 0).
* `enable-webhooks`: Enable or disable external notifications.
* `webhook-url`: Your Discord or custom API endpoint.
* `webhook-body`: The JSON payload to send, supporting variables like `%time%` and `%last_player_active%`.

## Building from Source

If you prefer to compile the binaries yourself, use the included Gradle wrapper:

1.  Clone the repository.
2.  Execute `./gradlew buildAll`.
3.  The resulting jars for all platforms will be located in the `dist/` folder.

## License

This project is licensed under the **MIT License**.
