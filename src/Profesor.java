import java.util.Scanner;

public class Profesor extends Persona {
    private String especialidad;
    private int aniosExperiencia;

    // TDA: arreglo para profesores
    private static Profesor[] listaProfesores = new Profesor[50];
    private static int contador = 0;

    // Para extender el arreglo (no obligatorio, se puede quitar si no se quiere ampliar)
    private static Scanner scanner = new Scanner(System.in);

    public static boolean agregarProfesor(Profesor p) {
        // Validar duplicados
        for (int i = 0; i < contador; i++) {
            Profesor existente = listaProfesores[i];
            if (existente.getNombre().equalsIgnoreCase(p.getNombre()) &&
                    existente.getApellido().equalsIgnoreCase(p.getApellido()) &&
                    existente.getFechaNacimiento().equals(p.getFechaNacimiento())) {
                return false; // duplicado
            }
        }

        // Validar espacio
        if (contador >= listaProfesores.length) {
            // Preguntar al usuario si desea ampliar el arreglo
            System.out.println("El arreglo de profesores está lleno. ¿Desea ampliar la capacidad? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                ampliarArreglo();
            } else {
                System.out.println("No se añadió el profesor porque no hay espacio.");
                return false;
            }
        }

        listaProfesores[contador++] = p;
        return true;
    }


    private static void ampliarArreglo() {
        Profesor[] nuevoArreglo = new Profesor[listaProfesores.length * 2];
        System.arraycopy(listaProfesores, 0, nuevoArreglo, 0, listaProfesores.length);
        listaProfesores = nuevoArreglo;
        System.out.println("Arreglo ampliado a " + listaProfesores.length + " posiciones.");
    }

    public static Profesor obtenerProfesor(int index) {
        if (index >= 0 && index < contador) {
            return listaProfesores[index];
        }
        return null;
    }

    public static int getCantidadProfesores() {
        return contador;
    }

    public static void reiniciarArreglo() {
        listaProfesores = new Profesor[50];
        contador = 0;
    }

    public static boolean existeProfesor(Profesor p) {
        for (int i = 0; i < contador; i++) {
            Profesor prof = listaProfesores[i];
            if (prof.getNombre().equalsIgnoreCase(p.getNombre())
                    && prof.getApellido().equalsIgnoreCase(p.getApellido())
                    && prof.getFechaNacimiento().equals(p.getFechaNacimiento())) {
                return true;
            }
        }
        return false;
    }


    public static void cargarDesdeBD(ProfesorDAO dao) {
        reiniciarArreglo();
        for (Profesor p : dao.listarProfesores()) {
            agregarProfesor(p);
        }
    }


    public static void guardarTodoEnBD(ProfesorDAO dao) {
        for (int i = 0; i < contador; i++) {
            Profesor p = listaProfesores[i];
            if (!dao.existeProfesorEnBD(p)) {
                dao.insertarProfesor(p);
            }
        }
    }

    // Constructor
    public Profesor(String nombre, String apellido, String fechaNacimiento, String genero,
                    double estatura, double peso, String especialidad, int aniosExperiencia) {
        super(nombre, apellido, fechaNacimiento, genero, estatura, peso);
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public void mostrarInformacion() {
        System.out.println("Profesor: " + getNombre() + " " + getApellido());
        System.out.println("Especialidad: " + especialidad + ", Años de experiencia: " + aniosExperiencia);
    }

 // Listar todos los profesores del TDA
 public static java.util.List<Profesor> listarProfesoresTDA() {
     java.util.List<Profesor> lista = new java.util.ArrayList<>();
     for (int i = 0; i < contador; i++) {
         lista.add(listaProfesores[i]);
     }
     return lista;
 }

 // Actualizar profesor en el TDA por nombre, apellido y fecha de nacimiento
 public static boolean actualizarProfesorTDA(String nombre, String apellido, String fechaNacimiento, Profesor nuevo) {
     for (int i = 0; i < contador; i++) {
         Profesor p = listaProfesores[i];
         if (p.getNombre().equalsIgnoreCase(nombre)
                 && p.getApellido().equalsIgnoreCase(apellido)
                 && p.getFechaNacimiento().equals(fechaNacimiento)) {
             listaProfesores[i] = nuevo;
             return true;
         }
     }
     return false;
 }

 // Eliminar profesor en el TDA por nombre, apellido y fecha de nacimiento
 public static boolean eliminarProfesorTDA(String nombre, String apellido, String fechaNacimiento) {
     for (int i = 0; i < contador; i++) {
         Profesor p = listaProfesores[i];
         if (p.getNombre().equalsIgnoreCase(nombre)
                 && p.getApellido().equalsIgnoreCase(apellido)
                 && p.getFechaNacimiento().equals(fechaNacimiento)) {
             for (int j = i; j < contador - 1; j++) {
                 listaProfesores[j] = listaProfesores[j + 1];
             }
             listaProfesores[--contador] = null;
             return true;
         }
     }
     return false;
 }


}


