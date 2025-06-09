import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProfesorDAO dao = new ProfesorDAO();

        // Cargar profesores desde la BD al arreglo TDA
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

        List<Profesor> profesoresTDA = Profesor.listarProfesoresTDA();
// Verifica si hay profesores en el TDA que no están en la BD
        boolean hayParaGuardar = false;
        profesoresTDA = Profesor.listarProfesoresTDA();
        for (Profesor p : profesoresTDA) {
            if (!dao.existeProfesorEnBD(p)) {
                hayParaGuardar = true;
                break;
            }
        }

        if (hayParaGuardar) {
            System.out.print("¿Desea guardar los profesores del TDA que no están en la base de datos? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                Profesor.guardarTodoEnBD(dao);
                System.out.println("Profesores guardados en la base de datos.");
            } else {
                System.out.println("No se guardaron los profesores del TDA en la base de datos.");
            }
        }

        // Mostrar profesores en TDA
        System.out.println("\n--- Profesores en arreglo TDA ---");
        for (int i = 0; i < Profesor.getCantidadProfesores(); i++) {
            Profesor.obtenerProfesor(i).mostrarInformacion();
        }

        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ INTERACTIVO ---");
            System.out.println("1. Listar profesores");
            System.out.println("2. Insertar nuevo profesor");
            System.out.println("3. Actualizar profesor");
            System.out.println("4. Eliminar profesor");
            System.out.println("5. Salir");
            System.out.println("6. Buscar profesor");

            boolean continuar = false;
            while (!continuar) {
                System.out.print("Seleccione una opción: ");
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
                    System.out.println("\n--- Listado de profesores (TDA / BD) ---");
                    profesoresTDA = Profesor.listarProfesoresTDA();
                    profesoresBD = dao.listarProfesores();

                    for (Profesor p : profesoresTDA) {
                        boolean enBD = profesoresBD.stream().anyMatch(pb ->
                            pb.getNombre().equalsIgnoreCase(p.getNombre()) &&
                            pb.getApellido().equalsIgnoreCase(p.getApellido()) &&
                            pb.getFechaNacimiento().equals(p.getFechaNacimiento())
                        );
                        System.out.print("[TDA" + (enBD ? " y BD" : "") + "] ");
                        p.mostrarInformacion();
                    }
                    for (Profesor p : profesoresBD) {
                        boolean enTDA = profesoresTDA.stream().anyMatch(pt ->
                            pt.getNombre().equalsIgnoreCase(p.getNombre()) &&
                            pt.getApellido().equalsIgnoreCase(p.getApellido()) &&
                            pt.getFechaNacimiento().equals(p.getFechaNacimiento())
                        );
                        if (!enTDA) {
                            System.out.print("[BD] ");
                            p.mostrarInformacion();
                        }
                    }
                    break;

                case 2:
                    System.out.println("¿Dónde desea insertar? 1. TDA  2. Base de datos");
                    int destinoInsertar = Integer.parseInt(scanner.nextLine());
                    Profesor nuevo = leerDatosProfesor();
                    if (destinoInsertar == 1) {
                        if (Profesor.existeProfesor(nuevo)) {
                            System.out.println("Error: Profesor ya existe en el arreglo TDA.");
                        } else if (!Profesor.agregarProfesor(nuevo)) {
                            System.out.println("Error: Arreglo TDA lleno.");
                        } else {
                            System.out.println("Profesor insertado en TDA.");
                        }
                    } else if (destinoInsertar == 2) {
                        if (dao.existeProfesorEnBD(nuevo)) {
                            System.out.println("Advertencia: Profesor ya existe en la base de datos.");
                        } else if (dao.insertarProfesor(nuevo)) {
                            System.out.println("Profesor insertado en la base de datos.");
                        } else {
                            System.out.println("Error al insertar en la base de datos.");
                        }
                    }
                    break;

                case 3:
                    System.out.println("¿Dónde desea actualizar? 1. TDA  2. Base de datos");
                    int destinoActualizar = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese el nombre del profesor a actualizar: ");
                    String nombreAct = scanner.nextLine();
                    System.out.print("Ingrese el apellido: ");
                    String apellidoAct = scanner.nextLine();
                    System.out.print("Ingrese la fecha de nacimiento (YYYY-MM-DD): ");
                    String fechaAct = scanner.nextLine();
                    Profesor actualizadoMenu = leerDatosProfesor();
                    if (destinoActualizar == 1) {
                        if (Profesor.actualizarProfesorTDA(nombreAct, apellidoAct, fechaAct, actualizadoMenu)) {
                            System.out.println("Profesor actualizado en TDA.");
                        } else {
                            System.out.println("Error al actualizar en TDA.");
                        }
                    } else if (destinoActualizar == 2) {
                        int id = dao.obtenerIdPorDatos(nombreAct, apellidoAct, fechaAct);
                        if (id != -1) {
                            if (dao.actualizarProfesor(id, actualizadoMenu)) {
                                System.out.println("Profesor actualizado en la base de datos.");
                            } else {
                                System.out.println("Error al actualizar en la base de datos.");
                            }
                        } else {
                            System.out.println("Profesor no encontrado.");
                        }
                    }
                    break;

                case 4:
                    System.out.println("¿Dónde desea eliminar? 1. TDA  2. Base de datos");
                    int destinoEliminar = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese el nombre del profesor a eliminar: ");
                    String nombreElim = scanner.nextLine();
                    System.out.print("Ingrese el apellido: ");
                    String apellidoElim = scanner.nextLine();
                    System.out.print("Ingrese la fecha de nacimiento (YYYY-MM-DD): ");
                    String fechaElim = scanner.nextLine();
                    if (destinoEliminar == 1) {
                        if (Profesor.eliminarProfesorTDA(nombreElim, apellidoElim, fechaElim)) {
                            System.out.println("Profesor eliminado del TDA.");
                        } else {
                            System.out.println("Error al eliminar en TDA.");
                        }
                    } else if (destinoEliminar == 2) {
                        int id = dao.obtenerIdPorDatos(nombreElim, apellidoElim, fechaElim);
                        if (id != -1) {
                            if (dao.eliminarProfesor(id)) {
                                System.out.println("Profesor eliminado de la base de datos.");
                            } else {
                                System.out.println("Error al eliminar en la base de datos.");
                            }
                        } else {
                            System.out.println("Profesor no encontrado.");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Saliendo...");
                    break;

                case 6:
                    System.out.println("Tipo de búsqueda: 1. Nombre 2. Apellido 3. Especialidad");
                    int tipoBusqueda = Integer.parseInt(scanner.nextLine());
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

    public static Profesor leerDatosProfesor() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        System.out.print("Género: ");
        String genero = scanner.nextLine();

        double estatura = esNumeroDouble("Estatura (en metros): ");

        double peso = esNumeroDouble("Peso (en kg): ");

        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();

        System.out.print("Años de experiencia: ");
        int anios = Integer.parseInt(scanner.nextLine());

        return new Profesor(nombre, apellido, fecha, genero, estatura, peso, especialidad, anios);
    }

    public static double esNumeroDouble(String texto) {
        boolean validador = false;
        String valorIngresado = "";
        double numeroReturn = 0.0;

        System.out.print(texto);

        while (!validador) {
            valorIngresado = scanner.nextLine();
            try {
                numeroReturn = Double.parseDouble(valorIngresado);
                validador = true;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        return numeroReturn;
    }
}