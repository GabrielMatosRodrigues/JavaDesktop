import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.ws.axis2.EstoqueBOStub;
import org.apache.ws.axis2.EstoqueBOStub.ConsultarProduto;
import org.apache.ws.axis2.EstoqueBOStub.ConsultarProdutoResponse;
import org.apache.ws.axis2.EstoqueBOStub.ProdutoTO;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

public class TelaConsulta {

	protected Shell shell;
	private Text text;

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TelaConsulta window = new TelaConsulta();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(223, 236);
		shell.setText("SWT Application");

		Label lblDigiteOCdigo = new Label(shell, SWT.NONE);
		lblDigiteOCdigo.setBounds(25, 20, 151, 15);
		lblDigiteOCdigo.setText("Digite o c\u00F3digo do produto");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(25, 51, 76, 21);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(25, 98, 149, 21);
		
		Button btnPesquisar = new Button(shell, SWT.NONE);
		btnPesquisar.setBounds(25, 125, 75, 25);
		btnPesquisar.setText("Pesquisar");
		btnPesquisar.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				int codigo = Integer.parseInt(text.getText());

				EstoqueBOStub stub = null;
				try {

					stub = new EstoqueBOStub();

				} catch (AxisFault e1) {
					MessageDialog.openInformation(shell, "SWT", "Produto não encontrado.");
				}
				ConsultarProduto consulta = new ConsultarProduto();
				consulta.setCodigo(codigo);
				ConsultarProdutoResponse response = null;
				
				try {
					response = stub.consultarProduto(consulta);
				}catch(AxisFault e1) {
					label.setText("Produto não encontrado.");
				}catch (RemoteException e1) {
					label.setText("Sistema fora do ar");
				}
				
				ProdutoTO produto = response.get_return();
				label.setText(produto.getDescricao());
			}
		});

		MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL);
		
		mb.setMessage("Clique OK caso queira encerrar a aplicação");
		int result = mb.open();
		if (result == SWT.OK) {
			System.out.println("OK foi pressionado");
			System.exit(0); // * encerra programa
		}
		if (result == SWT.CANCEL)
			System.out.println("cancela foi pressionado");

	}
}