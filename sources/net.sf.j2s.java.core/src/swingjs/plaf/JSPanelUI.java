package swingjs.plaf;


import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import swingjs.api.js.DOMNode;

public class JSPanelUI extends JSLightweightUI {

	int frameZ = 10000;
	public JSPanelUI() {
		isContainer = isPanel = true;
		setDoc();
	}
	
	@Override
	public DOMNode updateDOMNode() {
		if (domNode == null) {
			JRootPane root = jc.getRootPane();
			isContentPane = (root != null && jc == root.getContentPane());
			domNode = newDOMObject("div", id);
			if (root != null && root.getGlassPane() == c)
				DOMNode.setVisible(domNode,  false);
		}
		return updateDOMNodeCUI();
	}

	@Override
  protected Dimension getHTMLSizePreferred(DOMNode obj, boolean addCSS) {
		// SwingJS for now: just designated container width/height 
		return new Dimension(c.getWidth(), c.getHeight());
	}
	

	@Override
	public void installUI(JComponent jc) {
    LookAndFeel.installColorsAndFont(jc,
        "Panel.background",
        "Panel.foreground",
        "Panel.font");
	}

	@Override
	public void uninstallUI(JComponent jc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension getPreferredSize(JComponent jc) {
		return null;
  }
	
	@Override
	public Dimension getMinimumSize(JComponent jc) {
		LayoutManager man = jc.getLayout();
		return (man == null ? super.getMinimumSize(jc) : jc.getLayout().minimumLayoutSize(jc));
	}


}
