package camiseta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Camiseta {
	private String numeroLinea;
	private String cantidad;
	private String color;
	private String marca;
	private String modelo;
	private String talla;

	public Camiseta(String numeroLinea, String cantidad, String color, String marca, String modelo, String talla) {
		this.numeroLinea = numeroLinea;
		this.cantidad = cantidad;
		this.color = color;
		this.marca = marca;
		this.modelo = modelo;
		this.talla = talla;
	}

	public Camiseta() {
	}

	public String getNumeroLinea() {
		return numeroLinea;
	}

	public String getCantidad() {
		return cantidad;
	}

	public String getColor() {
		return color;
	}

	public String getMarca() {
		return marca;
	}

	public String getModelo() {
		return modelo;
	}

	public String getTalla() {
		return talla;
	}

	@Override
	public String toString() {
		return "Camiseta [numeroLinea=" + numeroLinea + ", cantidad=" + cantidad + ", color=" + color + ", marca="
				+ marca + ", modelo=" + modelo + ", talla=" + talla + "]";
	}

	private static Camiseta[] dameCamiseta(String line) {
		String[] parts = line.split(",");
		Camiseta[] camisetas = new Camiseta[1];

		camisetas[0] = new Camiseta(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(),
				parts[5].trim());

		return camisetas;
	}

	public static List<Camiseta> leerCamisetasValidas() {
		List<Camiseta> listaCamisetas = new ArrayList<>();
		try (BufferedReader bfr = new BufferedReader(new FileReader("src/camiseta.txt"))) {
			String line;
			int lineNumber = 0;
			while ((line = bfr.readLine()) != null) {
				lineNumber++;
				if (line.chars().filter(ch -> ch == ',').count() == 6) {
					Camiseta[] camisetas = dameCamiseta(line);
					listaCamisetas.add(camisetas[0]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listaCamisetas;
	}

	public static void filtrarArchivo(String inputFilePath, String outputFilePathSinErrores,
			String outputFilePathErrores) {
		List<String> validLines = new ArrayList<>();
		List<String> errorLines = new ArrayList<>();
		int totalLines = 0;
		int totalEliminadas = 0;

		try (BufferedReader bfr = new BufferedReader(new FileReader(inputFilePath))) {
			String line;
			while ((line = bfr.readLine()) != null) {
				totalLines++;
				if (line.chars().filter(ch -> ch == ',').count() == 6) {
					validLines.add(line);
				} else {
					errorLines.add(line);
					totalEliminadas++;
				}
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePathSinErrores))) {
				for (String validLine : validLines) {
					writer.write(validLine);
					writer.newLine();
				}
			}
			try (BufferedWriter errorWriter = new BufferedWriter(new FileWriter(outputFilePathErrores))) {
				errorWriter.write("Total líneas analizadas: " + totalLines + "\n");
				errorWriter.write("Total líneas eliminadas: " + totalEliminadas + "\n\n");
				errorWriter.write("Las líneas eliminadas son:\n");
				for (String errorLine : errorLines) {
					errorWriter.write(errorLine);
					errorWriter.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generarReporteFrecuencias(String filePath) {
		Map<String, Integer> cantidadFreq = new TreeMap<>();
		Map<String, Integer> colorFreq = new TreeMap<>();
		Map<String, Integer> marcaFreq = new TreeMap<>();
		Map<String, Integer> modeloFreq = new TreeMap<>();
		Map<String, Integer> tallaFreq = new TreeMap<>();

		try (BufferedReader bfr = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = bfr.readLine()) != null) {
				String[] parts = line.split(",");
				String cantidad = parts[1].trim();
				String color = parts[2].trim();
				String marca = parts[3].trim();
				String modelo = parts[4].trim();
				String talla = parts[5].trim();

				cantidadFreq.put(cantidad, cantidadFreq.getOrDefault(cantidad, 0) + 1);
				colorFreq.put(color, colorFreq.getOrDefault(color, 0) + 1);
				marcaFreq.put(marca, marcaFreq.getOrDefault(marca, 0) + 1);
				modeloFreq.put(modelo, modeloFreq.getOrDefault(modelo, 0) + 1);
				tallaFreq.put(talla, tallaFreq.getOrDefault(talla, 0) + 1);
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_frecuencias.txt"))) {
				writer.write("Reporte de frecuencias\n\n");

				writer.write("Cantidad:\n");
				cantidadFreq.forEach((key, value) -> {
					try {
						writer.write(key + ": " + value + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				writer.write("\nColor:\n");
				colorFreq.forEach((key, value) -> {
					try {
						writer.write(key + ": " + value + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				writer.write("\nMarca:\n");
				marcaFreq.forEach((key, value) -> {
					try {
						writer.write(key + ": " + value + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				writer.write("\nModelo:\n");
				modeloFreq.forEach((key, value) -> {
					try {
						writer.write(key + ": " + value + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

				writer.write("\nTalla:\n");
				tallaFreq.forEach((key, value) -> {
					try {
						writer.write(key + ": " + value + "\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void depurarDatos(String inputFilePath, String outputFilePath) {
		try (BufferedReader bfr = new BufferedReader(new FileReader(inputFilePath));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
			String line;
			while ((line = bfr.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 6) {
					// Validar y limpiar cada campo si es necesario antes de escribir
					String cleanedLine = String.join(",",
							Arrays.asList(parts).stream().map(String::trim).toArray(String[]::new));
					writer.write(cleanedLine);
					writer.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void generarArchivoSQL(String inputFilePath, String outputFilePath) {
		try (BufferedReader bfr = new BufferedReader(new FileReader(inputFilePath));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			writer.write("USE camisetas;\n");
			writer.write("INSERT INTO camisetas (cantidad, color, marca, modelo, talla) VALUES\n");

			String line;
			boolean firstLine = true;
			while ((line = bfr.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 6) {
					String cantidad = parts[1].trim();
					String color = parts[2].trim().replace("'", "''");
					String marca = parts[3].trim().replace("'", "''");
					String modelo = parts[4].trim().replace("'", "''");
					String talla = parts[5].trim().replace("'", "''");

					String sqlLine = String.format("(%s, '%s', '%s', '%s', '%s')", cantidad, color, marca, modelo,
							talla);

					if (!firstLine) {
						writer.write(",\n");
					}
					writer.write(sqlLine);
					firstLine = false;
				}
			}

			writer.write(";\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String inputFilePath = "src/camisetas.txt";
		String outputFilePathSinErrores = "src/camisetas_sin_errores_de_linea.txt";
		String outputFilePathErrores = "src/camisetas_con_errores_de_linea.log";

		filtrarArchivo(inputFilePath, outputFilePathSinErrores, outputFilePathErrores);

		generarReporteFrecuencias(outputFilePathSinErrores);

		String inputFilePathDepurado = "src/camisetas_sin_errores_de_linea.txt";
		String outputFilePathFinal = "src/camisetas_finales.txt";
		String outputSQLFilePath = "src/camisetas.sql";

		depurarDatos(inputFilePathDepurado, outputFilePathFinal);
		generarArchivoSQL(outputFilePathFinal, outputSQLFilePath);

		System.out.println("Archivos generados correctamente.");
		System.out.println("Procesamiento completado.");
	}

}
