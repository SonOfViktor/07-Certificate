import {InputAdornment, TextField} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import HoverIconButton from '../ui/HoverIconButton';

const SearchInput = props => {
  return (
    <TextField
      sx={{witdh: '100%', flexGrow: 1}}
      type="search"
      size="small"
      variant="outlined"
      name="search"
      placeholder="Search by name"
      {...props}
      InputProps={{
        endAdornment: (
          <InputAdornment position="end">
            <HoverIconButton edge="end">
              <SearchIcon sx={{fontSize: '1.5rem'}} />
            </HoverIconButton>
          </InputAdornment>
        ),
      }}
    />
  );
};

export default SearchInput;
