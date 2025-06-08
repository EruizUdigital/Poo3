import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProfesorDAO dao = new ProfesorDAO();
        Scanner scanner = new Scanner(System.in);

        // --- Cargar profesores desde la BD al arreglo TDA ---
        List<Profesor> profesoresBD = dao.listarProfesores();
        for (Profesor p : profesoresBD) {
            if (!Profesor.existeProfesor(p)) {
                if (!Profesor.agregarProfesor(p)) {
                    System.out.println("Arreglo TDA lleno al cargar desde BD.");
                    break;
                }
            }
        }

        System.out.println("Profesores cargados desde BD al arreglo TDA: " + Profesor.getCantidadProfesores());

        // --- Mostrar profesores en TDA ---
        System.out.println("\n--- Profesores en arreglo TDA ---");
        for (int i = 0; i < Profesor.getCantidadProfesores(); i++) {
            Profesor.obtenerProfesor(i).mostrarInformacion();
        }

        // Menú interactivo
        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ INTERACTIVO ---");
            System.out.println("1. Listar profesores");
            System.out.println("2. Insertar nuevo profesor");
            System.out.println("3. Actualizar profesor");
            System.out.println("4. Eliminar profesor");
            System.out.println("5. Salir");
            System.out.println("6. Buscar profesor");
            System.out.print("Seleccione una opción: ");

            boolean continuar = false;

            while(!continuar) {
                String entrada = scanner.nextLine();
                try {
                    opcion = Integer.parseInt(entrada);
                    continuar = true;
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número válido.");
                }
            }


            switch (opcion) {
                case 1:
                    System.out.println("\n--- Profesores en la base de datos ---");
                    List<Profesor> profesores = dao.listarProfesores();
                    for (Profesor p : profesores) {
                        p.mostrarInformacion();
                    }
                    break;

                case 2:
                    Profesor nuevo = leerDatosProfesor(scanner);

                    // Validar duplicado en TDA
                    if (Profesor.existeProfesor(nuevo)) {
                        System.out.println("Error: Profesor ya existe en el arreglo TDA.");
                        break;
                    }

                    // Validar si arreglo está lleno antes de agregar
                    if (!Profesor.agregarProfesor(nuevo)) {
                        System.out.println("Error: Arreglo TDA lleno, no se puede agregar más profesores.");
                        break;
                    }

                    // Validar duplicado en base de datos
                    if (dao.existeProfesorEnBD(nuevo)) {
                        System.out.println("Advertencia: Profesor ya existe en la base de datos, no se insertará.");
                    } else {
                        if (dao.insertarProfesor(nuevo)) {
                            System.out.println("Profesor insertado correctamente en la base de datos.");
                        } else {
                            System.out.println("Error al insertar en la base de datos.");
                        }
                    }
                    break;

                case 3:
                    System.out.print("Ingrese el ID del profesor a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    scanner.nextLine(); // limpiar buffer
                    Profesor actualizadoMenu = leerDatosProfesor(scanner);
                    if (dao.actualizarProfesor(idActualizar, actualizadoMenu)) {
                        System.out.println("Profesor actualizado.");
                    } else {
                        System.out.println("Error al actualizar.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese el ID del profesor a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    scanner.nextLine(); // limpiar buffer
                    if (dao.eliminarProfesor(idEliminar)) {
                        System.out.println("Profesor eliminado.");
                    } else {
                        System.out.println("Error al eliminar.");
                    }
                    break;

                case 5:
                    System.out.println("Saliendo del programa...");
                    break;

                case 6:
                    System.out.println("Buscar por: 1. Nombre  2. Apellido  3. Especialidad");
                    int tipoBusqueda = -1;
                    boolean continuarBuscar = false;

                    while(!continuar) {
                        String buscar = scanner.nextLine();
                        try {
                            tipoBusqueda = Integer.parseInt(buscar);
                            continuar = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Por favor, ingrese un número válido.");
                        }
                    }

                    System.out.print("Ingrese valor de búsqueda: ");
                    String criterio = scanner.nextLine();
                    List<Profesor> resultados = null;

                    switch (tipoBusqueda) {
                        case 1:
                            resultados = dao.buscarPorNombre(criterio);
                            break;
                        case 2:
                            resultados = dao.buscarPorApellido(criterio);
                            break;
                        case 3:
                            resultados = dao.buscarPorEspecialidad(criterio);
                            break;
                        default:
                            System.out.println("Opción de búsqueda inválida.");
                            resultados = List.of();
                            break;
                    }

                    if (!resultados.isEmpty()) {
                        System.out.println("\n--- Resultados de la búsqueda ---");
                        for (Profesor p : resultados) {
                            p.mostrarInformacion();
                        }
                    } else {
                        System.out.println("No se encontraron profesores.");
                    }
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 5);

        scanner.close();
    }

    public static Profesor leerDatosProfesor(Scanner scanner) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        System.out.print("Género: ");
        String genero = scanner.nextLine();

        System.out.print("Estatura (en metros): ");
        double estatura = scanner.nextDouble();

        System.out.print("Peso (en kg): ");
        double peso = scanner.nextDouble();
        scanner.nextLine(); // limpiar buffer

        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();

        System.out.print("Años de experiencia: ");
        int anios = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer

        return new Profesor(nombre, apellido, fecha, genero, estatura, peso, especialidad, anios);
    }
}
