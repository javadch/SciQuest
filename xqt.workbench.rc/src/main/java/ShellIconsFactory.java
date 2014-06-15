



import com.jidesoft.icons.IconsFactory;
import javax.swing.ImageIcon;


public class ShellIconsFactory {

    public static class Logos {
        public static final String TITLE_BAR32 = "icons/logos/titlebar32.png";
        public static final String LOGO32 = "icons/logos/logo32.gif";
        public static final String LOGO50 = "icons/logos/logo50.gif";
    }
    
    public static class Standard {
        public static final String ADD_NEW_ITEMS = "icons/standard/add_new_items.gif";
        public static final String OPEN = "icons/standard/open.gif";
        public static final String SAVE = "icons/standard/save.gif";
        public static final String SAVE_ALL = "icons/standard/save_all.gif";
        // ----

        public static final String CUT = "icons/standard/cut.gif";
        public static final String COPY = "icons/standard/copy.gif";
        public static final String PASTE = "icons/standard/paste.gif";
        // ----

        public static final String UNDO = "icons/standard/undo.gif";
        public static final String REDO = "icons/standard/redo.gif";
        public static final String NAVIGATE_BACKWARD = "icons/standard/navigate_backward.gif";
        public static final String NAVIGATE_FORWARD = "icons/standard/navigate_forward.gif";
        // ----

        public static final String START = "icons/standard/start.gif";
        // ----

        public static final String FIND_IN_FILES = "icons/standard/find_in_files.gif";
        // ----

        public static final String SOLUTION = "icons/toolwindows/solution_explorer.gif";
        public static final String PROPERTY = "icons/toolwindows/property.gif";
        public static final String TOOLBOX = "icons/toolwindows/toolbox.gif";

        // ---- // ----
        public static final String CLASSVIEW = "icons/toolwindows/class_view.gif";
        public static final String SERVER = "icons/toolwindows/server_explorer.gif";
        public static final String RESOURCEVIEW = "icons/toolwindows/resource_view.gif";

        // ---- // ----
        public static final String MACRO = "icons/toolwindows/macro_explorer.gif";
        public static final String OBJECT = "icons/toolwindows/object_browser.gif";
        public static final String DOCUMENTOUTLINE = "icons/toolwindows/document_outline.gif";

        // ---- // ----
        public static final String TASKLIST = "icons/toolwindows/tasklist.gif";
        public static final String COMMAND = "icons/toolwindows/command.gif";
        public static final String OUTPUT = "icons/toolwindows/output.gif";

        // ---- // ----
        public static final String FINDRESULT = "icons/toolwindows/find_result_1.gif";
        public static final String FINDSYMBOL = "icons/toolwindows/find_symbol_result.gif";

        // ---- // ----
        public static final String FAVORITES = "icons/toolwindows/favorites.gif";
    }

    public static class Build {
        public static final String BUILD_FILE = "icons/build/build_file.gif";
        public static final String BUILD_SOLUTION = "icons/build/build_solution.gif";
        public static final String CANCEL = "icons/build/cancel.gif";
    }

    public static class Layout {
        // ----

        public static final String ALIGN_TO_GRID = "icons/layout/align_to_grid.gif";
        // ----

        public static final String ALIGN_LEFTS = "icons/layout/align_lefts.gif";
        public static final String ALIGN_CENTERS = "icons/layout/align_centers.gif";
        public static final String ALIGN_RIGHTS = "icons/layout/align_rights.gif";
        // ----

        public static final String ALIGN_TOPS = "icons/layout/align_tops.gif";
        public static final String ALIGN_MIDDLES = "icons/layout/align_middles.gif";
        public static final String ALIGN_BOTTOMS = "icons/layout/align_bottoms.gif";
        // ----

        public static final String MAKE_SAME_WIDTH = "icons/layout/make_same_width.gif";
        public static final String SIZE_TO_GRID = "icons/layout/size_to_grid.gif";
        public static final String MAKE_SAME_HEIGHT = "icons/layout/make_same_height.gif";
        public static final String MAKE_SAME_SIZE = "icons/layout/make_same_size.gif";
        // ----

        public static final String MAKE_HORI_SPACING_EQUAL = "icons/layout/make_hori_spacing_equal.gif";
        public static final String INC_HORI_SPACING = "icons/layout/inc_hori_spacing.gif";
        public static final String DEC_HORI_SPACING = "icons/layout/dec_hori_spacing.gif";
        public static final String REMOVE_HORI_SPACING = "icons/layout/remove_hori_spacing.gif";
        // ----

        public static final String MAKE_VERT_SPACING_EQUAL = "icons/layout/make_vert_spacing_equal.gif";
        public static final String INC_VERT_SPACING = "icons/layout/inc_vert_spacing.gif";
        public static final String DEC_VERT_SPACING = "icons/layout/dec_vert_spacing.gif";
        public static final String REMOVE_VERT_SPACING = "icons/layout/remove_vert_spacing.gif";
        // ----

        public static final String CENTER_HORI = "icons/layout/center_hori.gif";
        public static final String CENTER_VERT = "icons/layout/center_vert.gif";
        // ----

        public static final String BRING_TO_FRONT = "icons/layout/bring_to_front.gif";
        public static final String SEND_TO_BACK = "icons/layout/send_to_back.gif";
    }

    public static class Formatting {
        // ----

        public static final String BOLD = "icons/formatting/bold.gif";
        public static final String ITALIC = "icons/formatting/italic.gif";
        public static final String UNDERLINE = "icons/formatting/underline.gif";
        // ----

        public static final String FOREGROUND = "icons/formatting/foreground.gif";
        public static final String BACKGROUND = "icons/formatting/background.gif";
        // ----

        public static final String ALIGN_LEFT = "icons/formatting/align-left.gif";
        public static final String ALIGN_CENTER = "icons/formatting/align-center.gif";
        public static final String ALIGN_RIGHT = "icons/formatting/align-right.gif";
        public static final String JUSTIFY = "icons/formatting/justify.gif";
        // ----

        public static final String NUMBERING = "icons/formatting/numbering.gif";
        public static final String BULLETS = "icons/formatting/bullets.gif";
        // ----

        public static final String DECREASE_INDENT = "icons/formatting/decrease-indent.gif";
        public static final String INCREASE_INDENT = "icons/formatting/increase-indent.gif";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(ShellIconsFactory.class, name);
        else
            return null;
    }


}
