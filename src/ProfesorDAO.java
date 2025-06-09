import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    public boolean insertarProfesor(Profesor profesor) {
        String sql = "INSERT INTO Profesores (nombre, apellido, fechaNacimiento, genero, estatura, peso, especialidad, aniosExperiencia) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellido());
            stmt.setString(3, profesor.getFechaNacimiento());
            stmt.setString(4, profesor.getGenero());
            stmt.setDouble(5, profesor.getEstatura());
            stmt.setDouble(6, profesor.getPeso());
            stmt.setString(7, profesor.getEspecialidad());
            stmt.setInt(8, profesor.getAniosExperiencia());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public List<Profesor> listarProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM Profesores";

        try (Connection conn = conexionSqlServer.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Profesor profesor = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("fechaNacimiento"),
                        rs.getString("genero"),
                        rs.getDouble("estatura"),
                        rs.getDouble("peso"),
                        rs.getString("especialidad"),
                        rs.getInt("aniosExperiencia")
                );
                profesores.add(profesor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }

        return profesores;
    }

    public boolean actualizarProfesor(int id, Profesor profesor) {
        String sql = "UPDATE Profesores SET nombre=?, apellido=?, fechaNacimiento=?, genero=?, estatura=?, peso=?, especialidad=?, aniosExperiencia=? WHERE id=?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellido());
            stmt.setString(3, profesor.getFechaNacimiento());
            stmt.setString(4, profesor.getGenero());
            stmt.setDouble(5, profesor.getEstatura());
            stmt.setDouble(6, profesor.getPeso());
            stmt.setString(7, profesor.getEspecialidad());
            stmt.setInt(8, profesor.getAniosExperiencia());
            stmt.setInt(9, id);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProfesor(int id) {
        String sql = "DELETE FROM Profesores WHERE id=?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public List<Profesor> buscarPorNombre(String nombre) {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM Profesores WHERE nombre LIKE ?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Profesor p = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("fechaNacimiento"),
                        rs.getString("genero"),
                        rs.getDouble("estatura"),
                        rs.getDouble("peso"),
                        rs.getString("especialidad"),
                        rs.getInt("aniosExperiencia")
                );
                profesores.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por nombre: " + e.getMessage());
        }

        return profesores;
    }

    public List<Profesor> buscarPorApellido(String apellido) {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM Profesores WHERE nombre LIKE ?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + apellido + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Profesor p = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("fechaNacimiento"),
                        rs.getString("genero"),
                        rs.getDouble("estatura"),
                        rs.getDouble("peso"),
                        rs.getString("especialidad"),
                        rs.getInt("aniosExperiencia")
                );
                profesores.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por apellido: " + e.getMessage());
        }

        return profesores;
    }

    public List<Profesor> buscarPorEspecialidad(String especialidad) {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT * FROM Profesores WHERE nombre LIKE ?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + especialidad + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Profesor p = new Profesor(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("fechaNacimiento"),
                        rs.getString("genero"),
                        rs.getDouble("estatura"),
                        rs.getDouble("peso"),
                        rs.getString("especialidad"),
                        rs.getInt("aniosExperiencia")
                );
                profesores.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por especialidad: " + e.getMessage());
        }

        return profesores;
    }

    public boolean existeProfesorEnBD(Profesor profesor) {
        String sql = "SELECT COUNT(*) FROM Profesores WHERE nombre = ? AND apellido = ? AND fechaNacimiento = ?";

        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, profesor.getNombre());
            stmt.setString(2, profesor.getApellido());
            stmt.setString(3, profesor.getFechaNacimiento());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // true si ya existe
            }
        } catch (SQLException e) {
            System.out.println("Error en existeProfesorEnBD: " + e.getMessage());
        }
        return false;
    }

    public int obtenerIdPorDatos(String nombre, String apellido, String fechaNacimiento) {
        String sql = "SELECT id FROM Profesores WHERE nombre=? AND apellido=? AND fechaNacimiento=?";
        try (Connection conn = conexionSqlServer.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, fechaNacimiento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener id: " + e.getMessage());
        }
        return -1; // No encontrado
    }

}
