# Добавление зависимостей [![](https://jitpack.io/v/maxos-void/DangerMine.svg)](https://jitpack.io/#maxos-void/DangerMine)

## Maven:
Добавить репозиторий pom.xml
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```
Добавьте зависимость
```xml
<dependency>
    <groupId>com.github.maxos-void</groupId>
    <artifactId>DangerMine</artifactId>
    <version>{version}</version>
</dependency>
```
## Gradle.kts:
Добавить репозиторий в конце списка
```kts
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url = uri("https://jitpack.io") }
	}
}
```
Добавьте зависимость
```kts
dependencies {
	implementation("com.github.maxos-void:DangerMine:{version}")
}
```

---

### **[MineBlockBreakEvent]**

**Описание:**  
Ивент вызывается, когда игрок удачно ломает блок в шахте (материал совпал)

**Пример:**
```java
@EventHandler
fun onBlockBreak(event: MineBlockBreakEvent) {
    val player = event.player // Игрок, сломавший блок
    val mine = event.mine // Шахта, в которой был сломан блок
    val block = event.block // Сам блок (физически он не ломается, поэтому ссылка рабочая)
    val dropItem = event.dropItem // ItemStack, который выпал при поломке блока
    // ваша логика
}
```

---

### **[MineBlockRestoredEvent]**

**Описание:**  
Ивент вызывается, когда сломанный блок восстанавливается (по таймеру или принудительно)

**Пример:**
```java
@EventHandler
fun onBlockRestored(event: MineBlockRestoredEvent) {
    val mine = event.mine // Шахта, в которой был восстановлен блок
    val block = event.block // Блок, который восстановился (ситуация такая же как с MineBlockBreakEvent)
    // ваша логика
}
```

---

### **[MineEnterEvent]**

**Описание:**  
Ивент вызывается, когда игрок успешно зашёл в регион шахты (и его не вытолкнуло)

**Пример:**
```java
@EventHandler
fun onEnterMine(event: MineEnterEvent) {
    val player = event.player // Игрок
    val mine = event.mine // Шахта, в которую зашёл игрок
    // ваша логика
}
```

---

### **[MineOpenEvent]**

**Описание:**  
Ивент вызывается при открытии шахты

**Пример:**
```java
@EventHandler
fun onMineOpen(event: MineOpenEvent) {
    val mine = event.mine // Шахта, которая открылась
    // ваша логика
}
```

---

### **[MineCloseEvent]**

**Описание:**  
Ивент вызывается при закрытии шахты

**Пример:**
```java
@EventHandler
fun onCloseMine(event: MineCloseEvent) {
    val mine = event.mine // Шахта, которая закрылась
    // ваша логика
}
```