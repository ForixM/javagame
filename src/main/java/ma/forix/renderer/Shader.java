package ma.forix.renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static Shader shader = new Shader("shader");
    public static Shader gui = new Shader("gui");
    public static Shader guiTex = new Shader("guiTex");

    private int program;
    private int vs;
    private int fs;

    private FloatBuffer theBuffer;

    public Shader(String filename){
        program = glCreateProgram();
        theBuffer = BufferUtils.createFloatBuffer(16);

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename+".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename+".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        glDetachShader(program, vs);
        glDetachShader(program, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
        glDeleteProgram(program);
        super.finalize();
    }

    public void setUniform(String name, int value){
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform1i(location, value);
    }

    public void setUniform(String name, Vector4f value){
        int location = glGetUniformLocation(program, name);
        if (location != -1)
            glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Matrix4f value){
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location != -1)
            glUniformMatrix4fv(location, false, buffer);
    }

    public void bind(){
        glUseProgram(program);
    }

    private String readFile(String filename){
        StringBuilder string = new StringBuilder();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(new File("./shaders/"+filename)));
            String line;
            while ((line = br.readLine()) != null){
                string.append(line);
                string.append("\n");
            }
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return string.toString();
    }
}
