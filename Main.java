import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
	static String pressedKeys = "";

	public static void main(String[] args) throws InterruptedException, IOException, ExecutionException {
		TerminalCanvas canvas = new TerminalCanvas(51, 19, TerminalColor.Black);
		TerminalInputHook input = new TerminalInputHook();

		Tetromino tetromino = new Tetromino_L_Shape(0);
		tetromino.relativeLocationX = 5;
		tetromino.relativeLocationY = 5;

		input.startHook(System.in);
		input.addListener(new TerminalInputListener() {
			@Override
			public void keyPressed(Character key) {
				pressedKeys += key;

				if (key == 'w') {
					tetromino.setRotation((tetromino.getRotation() + 1) % 4);
				}
			}
		});

		

		
		while (true) {
			/* 
			for (int ix = 0; ix < canvas.width(); ix += 3) {
				for (int iy = 0; iy < canvas.height(); iy++) {
					TerminalColor color = TerminalColor.randomColor();
					canvas.drawString(ix, iy, "###", color, color.negative());
				}
			}
			*/


			input.update();

			String canvasText = "=== Tolkmitte Test ===";
			canvas.drawString((canvas.width() / 2) - (canvasText.length() / 2), 0, canvasText, TerminalColor.randomColor(), TerminalColor.Black);

			String keyText = "Keys pressed: ";
			canvas.drawString(0, 1, keyText + pressedKeys + " ", TerminalColor.White, TerminalColor.Black);
			pressedKeys = "";
			tetromino.draw(canvas);
			//tetromino.relativeLocationY++;

			canvas.renderBuffer(System.out);
			Thread.sleep(100);
		}
	}
}
