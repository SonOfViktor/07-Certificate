import {
  Chip,
  InputAdornment,
  ListItem,
  ListItemIcon,
  ListItemText,
  TextField,
} from '@mui/material';
import FilterListIcon from '@mui/icons-material/FilterList';
import {useQueryParams} from '../hooks/useQueryParams';
import {useSelector} from 'react-redux';
import {Stack} from '@mui/system';
import ConfirmResetButton from '../../ui/ConfirmResetButton';
import {selectTags} from '../../../store/filter/filterSelectors';
import {useEffect, useState} from 'react';
import {useLocation} from 'react-router-dom';

const Filter = () => {
  const [changeQueryParams, initStateValue] = useQueryParams();
  const {pathname} = useLocation();
  const [description, setDescription] = useState(
    initStateValue('description', '')
  );
  const [tag, setTag] = useState('');
  const tags = useSelector(selectTags);

  useEffect(() => {
    return () => setDescription('');
  }, [pathname]);

  const handleDescriptionChange = e => {
    setDescription(e.target.value);
    changeQueryParams(e.target.name, e.target.value, 1000);
  };

  const handleAddTag = () => {
    if (!tags.includes(tag)) {
      changeQueryParams('tags', [...tags, tag].join(','));
    }
    setTag('');
  };

  const handleDeleteTag = deletedTag => {
    changeQueryParams('tags', tags.filter(tagElement => tagElement !== deletedTag).join(','));
  };

  return (
    <>
      <ListItem sx={{paddingBottom: 0}}>
        <ListItemIcon sx={{color: 'var(--primary)'}}>
          <FilterListIcon />
        </ListItemIcon>
        <ListItemText
          primary="Filter"
          primaryTypographyProps={{
            fontSize: 16,
            fontWeight: 500,
          }}
        />
      </ListItem>
      <ListItem>
        <TextField
          label="Description"
          name="description"
          size="small"
          value={description}
          onChange={handleDescriptionChange}
          fullWidth
        />
      </ListItem>

      <ListItem>
        <TextField
          id="tagField"
          name="tag"
          label="Tags"
          size="small"
          value={tag}
          onChange={e => setTag(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <ConfirmResetButton
                  sx={{marginRight: '-14px', boxShadow: 'none'}}
                  size="small"
                  onClick={handleAddTag}>
                  Add
                </ConfirmResetButton>
              </InputAdornment>
            ),
          }}
        />
      </ListItem>

      <ListItem divider>
        <Stack
          direction="row"
          flexWrap="wrap"
          gap={1}
          maxHeight={90}
          overflow="auto">
          {tags.map(tagElement => (
            <Chip
              key={tagElement}
              label={tagElement}
              size="small"
              variant="outlined"
              onDelete={() => handleDeleteTag(tagElement)}
            />
          ))}
        </Stack>
      </ListItem>
    </>
  );
};

export default Filter;
