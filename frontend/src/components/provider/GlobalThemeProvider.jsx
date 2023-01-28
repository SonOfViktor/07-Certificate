import {createTheme, ThemeProvider} from '@mui/material';

export const globalTheme = createTheme({
  palette: {
    primary: {
      light: '#66dc97',
      main: '#40d47e',
      dark: '#2c9458',
      contrastText: '#000',
    },
    secondary: {
      light: '#f98ed0',
      main: '#f872c5',
      dark: '#ad4f89',
      contrastText: '#000',
    },
  },
  components: {
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          '&:not(&.Mui-disabled):not(&.Mui-error):hover': {
            '&:hover fieldset': {borderColor: 'var(--primary)'},
          },
        },
      },
    },
    MuiSvgIcon: {
      styleOverrides: {
        root: {
          fontSize: '1.75rem',
          colorActive: 'var(--text-color)',
          cursor: 'pointer',
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          fontSize: '1rem',
        },
        head: {
          fontSize: '1.1rem',
        },
      },
    },
    MuiTablePagination: {
      styleOverrides: {
        select: {
          fontSize: '1rem',
        },
        selectLabel: {
          fontSize: '1rem',
        },
        displayedRows: {
          fontSize: '1rem',
        },
      },
    },
  },
  typography: {
    fontFamily: [
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
  },
});

const GlobalThemeProvider = ({children}) => {
  return <ThemeProvider theme={globalTheme}>{children}</ThemeProvider>;
};

export default GlobalThemeProvider;
